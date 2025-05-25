package com.example.frontend_mobile.ui.alumnos

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend_mobile.data.WebSocketManager
import com.example.frontend_mobile.data.model.Alumno
import com.example.frontend_mobile.data.repository.AlumnoRepository
import com.example.frontend_mobile.databinding.DialogAlumnoBinding
import com.example.frontend_mobile.databinding.FragmentAlumnosBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AlumnoFragment : Fragment(), AlumnoAdapter.OnAlumnoClickListener {

    private lateinit var binding: FragmentAlumnosBinding
    private val alumnoRepository = AlumnoRepository
    private lateinit var adapter: AlumnoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlumnosBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AlumnoAdapter(mutableListOf(), this, alumnoRepository)
        binding.recyclerViewAlumnos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAlumnos.adapter = adapter

        cargarAlumnos()

        WebSocketManager.conectar { tipo, evento, id ->
            if (tipo == "alumno" && (evento == "insertar" || evento == "actualizar" || evento == "eliminar")) {
                lifecycleScope.launch(Dispatchers.Main) {
                    cargarAlumnos()
                }
            }
        }

        binding.searchViewAlumnos.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true.also { filtrarAlumnos(query) }
            override fun onQueryTextChange(newText: String?) =
                true.also { filtrarAlumnos(newText) }
        })

        binding.fabAgregarAlumno.setOnClickListener {
            mostrarDialogAlumno(null)
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

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val alumno = adapter.listaAlumnos[pos]

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Eliminar alumno")
                            .setMessage("¿Eliminar ${alumno.nombre}?")
                            .setPositiveButton("Sí") { _, _ ->
                                lifecycleScope.launch {
                                    val exito = adapter.eliminarAlumno(pos)
                                    if (exito) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Alumno eliminada",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Error al eliminar la alumno",
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
                        mostrarDialogAlumno(alumno)
                    }
                }
            }
        })
        touchHelper.attachToRecyclerView(binding.recyclerViewAlumnos)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cargarAlumnos() {
        lifecycleScope.launch {
            val alumnosRemotos =
                alumnoRepository.listarAlumnos() // suspende y espera la respuesta
            adapter.actualizarLista(alumnosRemotos.toMutableList())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filtrarAlumnos(query: String?) {
        lifecycleScope.launch {
            val alumnos = alumnoRepository.listarAlumnos()
            val resultados = if (!query.isNullOrBlank()) {
                alumnos.filter {
                    it.nombre.contains(query, true) ||
                            it.cedula.contains(query, true)
                }
            } else {
                alumnos
            }
            adapter.actualizarLista(resultados.toMutableList())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onAlumnoClick(alumno: Alumno) {
        mostrarDialogAlumno(alumno)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onAlumnoLongClick(alumno: Alumno): Boolean {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar alumno")
            .setMessage("¿Estás seguro de eliminar la alumno ${alumno.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarAlumno(alumno)
            }
            .setNegativeButton("Cancelar", null)
            .show()
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun eliminarAlumno(alumno: Alumno) {
        lifecycleScope.launch {
            val alumnos = alumnoRepository.listarAlumnos().toMutableList()
            alumnos.remove(alumno)
            adapter.actualizarLista(alumnos)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Alumno eliminada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrarDialogAlumno(alumno: Alumno?) {
        val dialogBinding = DialogAlumnoBinding.inflate(layoutInflater)
        alumno.let {
            dialogBinding.etCedulaAlumno.setText(it?.cedula)
            dialogBinding.etNombreAlumno.setText(it?.nombre)
            dialogBinding.etTelefonoAlumno.setText(it?.telefono)
            dialogBinding.etEmailAlumno.setText(it?.email)
            dialogBinding.etFechaNacimientoAlumno.text = Editable.Factory.getInstance().newEditable(it?.fechaNacimiento?.toString() ?: "")
            dialogBinding.etCodigoCarreraAlumno.setText(it?.codigoCarrera)
        }

        AlertDialog.Builder(requireContext())
            .setTitle(if (alumno == null) "Agregar Alumno" else "Editar Alumno")
            .setView(dialogBinding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val cedula = dialogBinding.etCedulaAlumno.text.toString().trim()
                val nombre = dialogBinding.etNombreAlumno.text.toString().trim()
                val telefono = dialogBinding.etTelefonoAlumno.text.toString().trim()
                val email = dialogBinding.etEmailAlumno.text.toString().trim()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val fechaNacimiento = LocalDate.parse(dialogBinding.etFechaNacimientoAlumno.text.toString(), formatter)
                val codigoCarrera = dialogBinding.etCodigoCarreraAlumno.text.toString().trim()

                lifecycleScope.launch {
                    if (alumno == null) {
                        val nuevoAlumno = Alumno(cedula, nombre, telefono, email, fechaNacimiento, codigoCarrera)
                        try {
                            val exito = alumnoRepository.agregarAlumno(nuevoAlumno)
                            if (exito) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Alumno agregada",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    cargarAlumnos()
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    } else {
                        val alumnosActualizados = alumnoRepository.listarAlumnos().map {
                            if (it.cedula == alumno.cedula) Alumno(
                                cedula,
                                nombre,
                                telefono,
                                email,
                                fechaNacimiento,
                                codigoCarrera
                            ) else it
                        }
                        alumnoRepository.setAlumnos(alumnosActualizados)
                        withContext(Dispatchers.Main) {
                            adapter.actualizarLista(alumnosActualizados.toMutableList())
                            Toast.makeText(
                                requireContext(),
                                "Alumno actualizado",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
