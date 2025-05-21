package com.example.quiz.ui.profesores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz.data.model.Profesor
import com.example.quiz.data.repository.ProfesorRepository
import com.example.quiz.databinding.DialogProfesorBinding
import com.example.quiz.databinding.FragmentProfesoresBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfesorFragment : Fragment(), ProfesorAdapter.OnProfesorClickListener {

    private lateinit var binding: FragmentProfesoresBinding
    private val profesorRepository = ProfesorRepository
    private lateinit var adapter: ProfesorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfesoresBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ProfesorAdapter(mutableListOf(), this, profesorRepository)
        binding.recyclerViewProfesores.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewProfesores.adapter = adapter

        cargarProfesores()

        binding.searchViewProfesores.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true.also { filtrarProfesores(query) }
            override fun onQueryTextChange(newText: String?) =
                true.also { filtrarProfesores(newText) }
        })

        binding.fabAgregarProfesor.setOnClickListener {
            mostrarDialogProfesor(null)
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
                val profesor = adapter.listaProfesores[pos]

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Eliminar profesor")
                            .setMessage("¿Eliminar ${profesor.nombre}?")
                            .setPositiveButton("Sí") { _, _ ->
                                lifecycleScope.launch {
                                    val exito = adapter.eliminarProfesor(pos)
                                    if (exito) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Profesor eliminado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Error al eliminar el profesor",
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
                        mostrarDialogProfesor(profesor)
                    }
                }
            }
        })
        touchHelper.attachToRecyclerView(binding.recyclerViewProfesores)
    }

    private fun cargarProfesores() {
        lifecycleScope.launch {
            val profesoresRemotos =
                profesorRepository.listarProfesores() // suspende y espera la respuesta
            adapter.actualizarLista(profesoresRemotos.toMutableList())
        }
    }

    private fun filtrarProfesores(query: String?) {
        lifecycleScope.launch {
            val profesores = profesorRepository.listarProfesores()
            val resultados = if (!query.isNullOrBlank()) {
                profesores.filter {
                    it.nombre.contains(query, true) ||
                            it.cedula.contains(query, true)
                }
            } else {
                profesores
            }
            adapter.actualizarLista(resultados.toMutableList())
        }
    }

    override fun onProfesorClick(profesor: Profesor) {
        mostrarDialogProfesor(profesor)
    }

    override fun onProfesorLongClick(profesor: Profesor): Boolean {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar profesor")
            .setMessage("¿Estás seguro de eliminar el profesor ${profesor.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarProfesor(profesor)
            }
            .setNegativeButton("Cancelar", null)
            .show()
        return true
    }

    private fun eliminarProfesor(profesor: Profesor) {
        lifecycleScope.launch {
            val profesores = profesorRepository.listarProfesores().toMutableList()
            profesores.remove(profesor)
            adapter.actualizarLista(profesores)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Profesor eliminado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarDialogProfesor(profesor: Profesor?) {
        val dialogBinding = DialogProfesorBinding.inflate(layoutInflater)
        profesor.let {
            dialogBinding.etCedulaProfesor.setText(it?.cedula)
            dialogBinding.etNombreProfesor.setText(it?.nombre)
            dialogBinding.etTelefono.setText(it?.telefono)
            dialogBinding.etEmail.setText(it?.email)
        }

        AlertDialog.Builder(requireContext())
            .setTitle(if (profesor == null) "Agregar Profesor" else "Editar Profesor")
            .setView(dialogBinding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val cedula = dialogBinding.etCedulaProfesor.text.toString().trim()
                val nombre = dialogBinding.etNombreProfesor.text.toString().trim()
                val telefono = dialogBinding.etTelefono.text.toString().trim()
                val email = dialogBinding.etEmail.text.toString().trim()

                lifecycleScope.launch {
                    if (profesor == null) {
                        val nuevoProfesor = Profesor(cedula, nombre, telefono, email)
                        try {
                            val exito = profesorRepository.agregarProfesor(nuevoProfesor)
                            if (exito) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Profesor agregado",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    cargarProfesores()
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    } else {
                        val profesoresActualizados = profesorRepository.listarProfesores().map {
                            if (it.cedula == profesor.cedula) Profesor(
                                cedula,
                                nombre,
                                telefono,
                                email
                            ) else it
                        }
                        profesorRepository.setProfesores(profesoresActualizados)
                        withContext(Dispatchers.Main) {
                            adapter.actualizarLista(profesoresActualizados.toMutableList())
                            Toast.makeText(
                                requireContext(),
                                "Profesor actualizado",
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
