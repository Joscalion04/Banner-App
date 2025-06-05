package com.example.frontend_mobile.ui.grupos

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend_mobile.R
import com.example.frontend_mobile.data.model.Carrera
import com.example.frontend_mobile.data.model.Ciclo
import com.example.frontend_mobile.data.model.Curso
import com.example.frontend_mobile.data.model.Grupo
import com.example.frontend_mobile.data.model.CarreraCurso
import com.example.frontend_mobile.data.repository.CarreraRepository
import com.example.frontend_mobile.data.repository.CicloRepository
import com.example.frontend_mobile.data.repository.CursoRepository
import com.example.frontend_mobile.data.repository.GrupoRepository
import com.example.frontend_mobile.data.repository.CarreraCursoRepository
import com.example.frontend_mobile.databinding.DialogGrupoBinding
import com.example.frontend_mobile.databinding.FragmentGruposBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GrupoFragment : Fragment(), GrupoAdapter.OnGrupoClickListener {

    var listener: GrupoAdapter.OnGrupoClickListener = this
    private lateinit var binding: FragmentGruposBinding
    private val carreraRepository = CarreraRepository
    private val cicloRepository = CicloRepository
    private val cursoRepository = CursoRepository
    private val grupoRepository = GrupoRepository
    private val carreraCursoRepository = CarreraCursoRepository
    private lateinit var adapter: GrupoAdapter

    private var listaCarreras: List<Carrera> = emptyList()
    private var listaCiclos: List<Ciclo> = emptyList()
    private var listaCursos: List<Curso> = emptyList()
    private var listaCarrerasCursos: List<CarreraCurso> = emptyList()
    private var todosLosGrupos: List<Grupo> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGruposBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ocultar botón de cerrar en modo fragmento normal
        binding.root.findViewById<View>(R.id.btnClose)?.visibility = View.GONE

        // Resto de la configuración original...
        adapter = GrupoAdapter(mutableListOf(), listener)
        binding.recyclerViewGrupos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewGrupos.adapter = adapter

        cargarDatos()
        configurarSpinners()
        configurarFab()
        configurarSwipeGestures()
    }

    override fun isDialogMode(): Boolean {
        // En el fragmento original, nunca estamos en modo diálogo
        return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cargarDatos() {
        lifecycleScope.launch {
            listaCarreras = carreraRepository.listarCarreras()
            listaCiclos = cicloRepository.listarCiclos()
            listaCursos = cursoRepository.listarCursos()
            listaCarrerasCursos = carreraCursoRepository.listarCarrerasCursos()
            todosLosGrupos = grupoRepository.listarGrupos()

            withContext(Dispatchers.Main) {
                configurarAdaptersSpinners()
                actualizarEstadoFabSegunSeleccion()
            }
        }
    }

    private fun configurarAdaptersSpinners() {
        // Configurar spinner de carreras
        val adapterCarreras = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listaCarreras)
        adapterCarreras.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCarrera.adapter = adapterCarreras

        // Configurar spinner de ciclos
        val adapterCiclos = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listaCiclos)
        adapterCiclos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCiclo.adapter = adapterCiclos

        // Configurar spinner de cursos (inicialmente vacío)
        val adapterCursos = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mutableListOf<Curso>())
        adapterCursos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCurso.adapter = adapterCursos
    }

    private fun configurarSpinners() {
        binding.spinnerCarrera.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                actualizarCursos()
                filtrarGrupos()
                actualizarEstadoFabSegunSeleccion()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerCiclo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                actualizarCursos()
                filtrarGrupos()
                actualizarEstadoFabSegunSeleccion()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerCurso.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filtrarGrupos()
                actualizarEstadoFabSegunSeleccion()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun actualizarEstadoFabSegunSeleccion() {
        val carreraSeleccionada = listaCarreras.getOrNull(binding.spinnerCarrera.selectedItemPosition)
        val cicloSeleccionado = listaCiclos.getOrNull(binding.spinnerCiclo.selectedItemPosition)
        val cursoSeleccionado = (binding.spinnerCurso.adapter as? ArrayAdapter<Curso>)
            ?.takeIf { binding.spinnerCurso.selectedItemPosition in 0 until (binding.spinnerCurso.adapter?.count ?: 0) }
            ?.getItem(binding.spinnerCurso.selectedItemPosition)

        val habilitarFab = carreraSeleccionada != null && cicloSeleccionado != null && cursoSeleccionado != null
        binding.fabAgregarGrupo.visibility = if (habilitarFab) View.VISIBLE else View.GONE
    }

    private fun actualizarCursos() {
        val carreraSeleccionada = listaCarreras.getOrNull(binding.spinnerCarrera.selectedItemPosition)
        val cicloSeleccionado = listaCiclos.getOrNull(binding.spinnerCiclo.selectedItemPosition)

        if (carreraSeleccionada != null && cicloSeleccionado != null) {
            val codigosCursos = listaCarrerasCursos
                .filter { it.codigoCarrera == carreraSeleccionada.codigoCarrera && it.ciclo == cicloSeleccionado.cicloId }
                .map { it.codigoCurso }

            val cursosFiltrados = listaCursos.filter { it.codigoCurso in codigosCursos }

            (binding.spinnerCurso.adapter as ArrayAdapter<Curso>).apply {
                clear()
                addAll(cursosFiltrados)
                notifyDataSetChanged()
            }
        }
    }

    private fun filtrarGrupos() {
        val posicionCiclo = binding.spinnerCiclo.selectedItemPosition
        val posicionCurso = binding.spinnerCurso.selectedItemPosition

        val cicloSeleccionado = listaCiclos.getOrNull(posicionCiclo)
        val cursoSeleccionado = (binding.spinnerCurso.adapter as? ArrayAdapter<Curso>)
            ?.takeIf { posicionCurso in 0 until it.count }
            ?.getItem(posicionCurso)

        if (cicloSeleccionado != null && cursoSeleccionado != null) {
            val gruposFiltrados = todosLosGrupos.filter { grupo ->
                grupo.cicloId == cicloSeleccionado.cicloId &&
                grupo.codigoCurso == cursoSeleccionado.codigoCurso
            }
            adapter.actualizarLista(gruposFiltrados)
        } else {
            adapter.actualizarLista(emptyList())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun configurarFab() {
        binding.fabAgregarGrupo.setOnClickListener {
            mostrarDialogGrupo(null)
        }
    }

    private fun configurarSwipeGestures() {
        val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val grupo = adapter.grupos[position]

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        mostrarDialogEliminar(grupo, position)
                    }
                    ItemTouchHelper.RIGHT -> {
                        adapter.notifyItemChanged(position)
                        mostrarDialogGrupo(grupo)
                    }
                }
            }
        })
        touchHelper.attachToRecyclerView(binding.recyclerViewGrupos)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrarDialogEliminar(grupo: Grupo, position: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar grupo")
            .setMessage("¿Eliminar el grupo ${grupo.numeroGrupo}?")
            .setPositiveButton("Sí") { _, _ ->
                lifecycleScope.launch {
                    val exito = grupoRepository.eliminarGrupo(grupo)
                    withContext(Dispatchers.Main) {
                        if (exito) {
                            adapter.eliminarGrupo(position)
                            Toast.makeText(requireContext(), "Grupo eliminado", Toast.LENGTH_SHORT).show()
                            // Recargar datos para actualizar la lista
                            cargarDatos()
                        } else {
                            Toast.makeText(requireContext(), "Error al eliminar grupo", Toast.LENGTH_SHORT).show()
                            adapter.notifyItemChanged(position)
                        }
                    }
                }
            }
            .setNegativeButton("No") { _, _ ->
                adapter.notifyItemChanged(position)
            }
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrarDialogGrupo(grupo: Grupo?) {
        val dialogBinding = DialogGrupoBinding.inflate(layoutInflater)

        // Prellenar campos si es edición
        grupo?.let {
            dialogBinding.etNumeroGrupo.setText(it.numeroGrupo.toString())
            dialogBinding.etHorario.setText(it.horario)
            dialogBinding.etCedulaProfesor.setText(it.cedulaProfesor)
        }

        AlertDialog.Builder(requireContext())
            .setTitle(if (grupo == null) "Agregar Grupo" else "Editar Grupo")
            .setView(dialogBinding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val cicloSeleccionado = listaCiclos.getOrNull(binding.spinnerCiclo.selectedItemPosition)
                val cursoSeleccionado = (binding.spinnerCurso.adapter as? ArrayAdapter<Curso>)
                    ?.getItem(binding.spinnerCurso.selectedItemPosition)

                if (cicloSeleccionado == null || cursoSeleccionado == null) {
                    Toast.makeText(requireContext(), "Debe seleccionar ciclo y curso", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val numeroGrupo = dialogBinding.etNumeroGrupo.text.toString().toIntOrNull()
                val horario = dialogBinding.etHorario.text.toString().trim()
                val cedulaProfesor = dialogBinding.etCedulaProfesor.text.toString().trim()

                if (numeroGrupo == null || horario.isEmpty() || cedulaProfesor.isEmpty()) {
                    Toast.makeText(requireContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                lifecycleScope.launch {
                    val nuevoGrupo = Grupo(
                        grupoId = grupo?.grupoId ?: 0,
                        cicloId = cicloSeleccionado.cicloId,
                        codigoCurso = cursoSeleccionado.codigoCurso,
                        numeroGrupo = numeroGrupo,
                        horario = horario,
                        cedulaProfesor = cedulaProfesor
                    )

                    val exito = if (grupo == null) {
                        grupoRepository.agregarGrupoRemoto(nuevoGrupo)
                    } else {
                        grupoRepository.setGrupos(listOf(nuevoGrupo))
                    }

                    withContext(Dispatchers.Main) {
                        if (exito) {
                            Toast.makeText(requireContext(), "Guardado correctamente", Toast.LENGTH_SHORT).show()
                            cargarDatos()
                            filtrarGrupos()
                        } else {
                            Toast.makeText(requireContext(), "Error al guardar", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onGrupoClick(grupo: Grupo) {
        mostrarDialogGrupo(grupo)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onGrupoLongClick(grupo: Grupo): Boolean {
        val position = adapter.grupos.indexOf(grupo)
        if (position >= 0) {
            mostrarDialogEliminar(grupo, position)
        }
        return true
    }
}