package com.example.frontend_mobile.ui.cursos

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend_mobile.data.WebSocketManager
import com.example.frontend_mobile.data.model.CarreraCurso
import com.example.frontend_mobile.data.model.Curso
import com.example.frontend_mobile.data.repository.CarreraCursoRepository
import com.example.frontend_mobile.data.repository.CarreraRepository
import com.example.frontend_mobile.data.repository.CicloRepository
import com.example.frontend_mobile.data.repository.CursoRepository
import com.example.frontend_mobile.databinding.DialogCursoBinding
import com.example.frontend_mobile.databinding.FragmentCursosBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CursoFragment : Fragment(), CursoAdapter.OnCursoClickListener {

    private lateinit var binding: FragmentCursosBinding
    private val cursoRepository = CursoRepository
    private val carreraRepository = CarreraRepository
    private val cicloRepository = CicloRepository
    private val carrerasCursosRepository = CarreraCursoRepository
    private lateinit var adapter: CursoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCursosBinding.inflate(inflater, container, false)
        cursoRepository.init(requireContext().applicationContext)
        carreraRepository.init(requireContext().applicationContext)
        cicloRepository.init(requireContext().applicationContext)
        carrerasCursosRepository.init(requireContext().applicationContext)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CursoAdapter(mutableListOf(), this, cursoRepository, carreraRepository, cicloRepository, carrerasCursosRepository)
        binding.recyclerViewCursos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCursos.adapter = adapter

        cargarCursos()

        WebSocketManager.conectar { tipo, evento, id ->
            if (tipo == "curso" && (evento == "insertar" || evento == "actualizar" || evento == "eliminar")) {
                lifecycleScope.launch(Dispatchers.Main) {
                    cargarCursos()
                }
            }
        }

        binding.searchViewCursos.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onQueryTextSubmit(query: String?) = true.also { filtrarCursos(query) }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onQueryTextChange(newText: String?) = true.also { filtrarCursos(newText) }
        })

        binding.fabAgregarCurso.setOnClickListener {
            mostrarDialogCurso(null)
        }

        val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val curso = adapter.cursos[pos]

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Eliminar curso")
                            .setMessage("¿Eliminar ${curso.nombre}?")
                            .setPositiveButton("Sí") { _, _ ->
                                lifecycleScope.launch {
                                    val exito = adapter.eliminarCurso(pos)
                                    if (exito) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Curso eliminado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Error al eliminar curso",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        adapter.notifyItemChanged(pos) // para "revertir" swipe visualmente
                                    }
                                }
                            }
                            .setNegativeButton("No") { _, _ ->
                                adapter.notifyItemChanged(pos)
                            }
                            .show()
                    }

                    ItemTouchHelper.RIGHT -> {
                        adapter.notifyItemChanged(pos)
                        mostrarDialogCurso(curso)
                    }
                }
            }
        })
        touchHelper.attachToRecyclerView(binding.recyclerViewCursos)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cargarCursos() {
        lifecycleScope.launch {
            val cursosRemotos = cursoRepository.listarCursos() // suspende y espera la respuesta
            adapter.actualizarLista(cursosRemotos.toMutableList())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filtrarCursos(query: String?) {
        lifecycleScope.launch {
            val cursos = cursoRepository.listarCursos()
            val resultados = if (!query.isNullOrBlank()) {
                cursos.filter {
                    it.nombre.contains(query, true) ||
                            it.codigoCurso.contains(query, true)
                }
            } else {
                cursos
            }
            adapter.actualizarLista(resultados.toMutableList())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCursoClick(curso: Curso) {
        mostrarDialogCurso(curso)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCursoLongClick(curso: Curso): Boolean {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar curso")
            .setMessage("¿Estás seguro de eliminar el curso ${curso.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarCurso(curso)
            }
            .setNegativeButton("Cancelar", null)
            .show()
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun eliminarCurso(curso: Curso) {
        lifecycleScope.launch {
            val cursos = cursoRepository.listarCursos().toMutableList()
            cursos.remove(curso)
            adapter.actualizarLista(cursos)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Curso eliminado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrarDialogCurso(curso: Curso?) {
        val dialogBinding = DialogCursoBinding.inflate(layoutInflater)

        lifecycleScope.launch {
            val listaCarreras = carreraRepository.listarCarreras()
            val listaCiclos = cicloRepository.listarCiclos()
            val listaCarrerasCursos = carrerasCursosRepository.listarCarrerasCursos()

            val carreraAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listaCarreras)
            carreraAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dialogBinding.etSpinnerCarreraCurso.adapter = carreraAdapter

            val cicloAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listaCiclos)
            cicloAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dialogBinding.etSpinnerCicloCurso.adapter = cicloAdapter

            // Prellenar campos si hay curso
            curso?.let {
                dialogBinding.etNombreCurso.setText(it.nombre)
                dialogBinding.etCreditos.setText(it.creditos.toString())
                dialogBinding.etHorasSemanales.setText(it.horasSemanales.toString())

                val relacion = listaCarrerasCursos.firstOrNull()

                if (relacion != null) {
                    val idxCarrera = listaCarreras.indexOfFirst { c -> c.codigoCarrera == relacion.codigoCarrera }
                    if (idxCarrera != -1) dialogBinding.etSpinnerCarreraCurso.setSelection(idxCarrera)

                    val idxCiclo = listaCiclos.indexOfFirst { c -> c.cicloId == relacion.ciclo }
                    if (idxCiclo != -1) dialogBinding.etSpinnerCicloCurso.setSelection(idxCiclo)
                } else {
                    dialogBinding.etSpinnerCarreraCurso.setSelection(0)
                    dialogBinding.etSpinnerCicloCurso.setSelection(0)
                }
            }

            AlertDialog.Builder(requireContext())
                .setTitle(if (curso == null) "Agregar Curso" else "Editar Curso")
                .setView(dialogBinding.root)
                .setPositiveButton("Guardar") { _, _ ->
                    val nombre = dialogBinding.etNombreCurso.text.toString()
                    val creditos = dialogBinding.etCreditos.text.toString().toIntOrNull() ?: 0
                    val horas = dialogBinding.etHorasSemanales.text.toString().toIntOrNull() ?: 0
                    val carrera = listaCarreras[dialogBinding.etSpinnerCarreraCurso.selectedItemPosition]
                    val ciclo = listaCiclos[dialogBinding.etSpinnerCicloCurso.selectedItemPosition]

                    lifecycleScope.launch {
                        if (curso == null) {
                            val nuevoCurso = Curso(cursoRepository.generarCodigoCurso(), nombre, creditos, horas)
                            try {
                                val exito = cursoRepository.agregarCursoRemoto(nuevoCurso)
                                if (exito) {
                                    carrerasCursosRepository.agregarCarreraCurso(CarreraCurso(
                                        null,
                                        carrera.codigoCarrera,
                                        nuevoCurso.codigoCurso,
                                        ciclo.anio,
                                        ciclo.cicloId,
                                        1
                                    ))
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(requireContext(), "Curso agregado", Toast.LENGTH_SHORT).show()
                                        cargarCursos()
                                    }
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            val cursosActualizados = cursoRepository.listarCursos().map {
                                if (it.codigoCurso == curso.codigoCurso) {
                                    Curso(curso.codigoCurso, nombre, creditos, horas)
                                } else it
                            }
                            cursoRepository.setCursos(cursosActualizados)

                            val carreraCurso = carrerasCursosRepository.listarCarrerasCursos().first { it.codigoCurso == curso.codigoCurso }

                            carrerasCursosRepository.setCarrerasCursos(listOf(CarreraCurso(
                                carreraCurso.carreraCursoId,
                                carrera.codigoCarrera,
                                curso.codigoCurso,
                                ciclo.anio,
                                ciclo.cicloId,
                                carreraCurso.orden
                            )))

                            withContext(Dispatchers.Main) {
                                adapter.actualizarLista(cursosActualizados.toMutableList())
                                Toast.makeText(requireContext(), "Curso actualizado", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}
