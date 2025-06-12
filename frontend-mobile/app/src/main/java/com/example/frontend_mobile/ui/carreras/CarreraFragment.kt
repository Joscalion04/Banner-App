package com.example.frontend_mobile.ui.carreras

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
import com.example.frontend_mobile.data.WebSocketManager
import com.example.frontend_mobile.data.model.Carrera
import com.example.frontend_mobile.data.repository.CarreraRepository
import com.example.frontend_mobile.databinding.DialogCarreraBinding
import com.example.frontend_mobile.databinding.FragmentCarrerasBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarreraFragment : Fragment(), CarreraAdapter.OnCarreraClickListener {

    private lateinit var binding: FragmentCarrerasBinding
    private val carreraRepository = CarreraRepository
    private lateinit var adapter: CarreraAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCarrerasBinding.inflate(inflater, container, false)
        carreraRepository.init(requireContext().applicationContext)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CarreraAdapter(mutableListOf(), this, carreraRepository)
        binding.recyclerViewCarreras.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCarreras.adapter = adapter

        cargarCarreras()

        WebSocketManager.conectar { tipo, evento, id ->
            if (tipo == "carrera" && (evento == "insertar" || evento == "actualizar" || evento == "eliminar")) {
                lifecycleScope.launch(Dispatchers.Main) {
                    cargarCarreras()
                }
            }
        }

        binding.searchViewCarreras.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true.also { filtrarCarreras(query) }
            override fun onQueryTextChange(newText: String?) =
                true.also { filtrarCarreras(newText) }
        })

        binding.fabAgregarCarrera.setOnClickListener {
            mostrarDialogCarrera(null)
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
                val carrera = adapter.listaCarreras[pos]

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Eliminar carrera")
                            .setMessage("¿Eliminar ${carrera.nombre}?")
                            .setPositiveButton("Sí") { _, _ ->
                                lifecycleScope.launch {
                                    val exito = adapter.eliminarCarrera(pos)
                                    if (exito) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Carrera eliminada",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Error al eliminar la carrera",
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
                        mostrarDialogCarrera(carrera)
                    }
                }
            }
        })
        touchHelper.attachToRecyclerView(binding.recyclerViewCarreras)
    }

    private fun cargarCarreras() {
        lifecycleScope.launch {
            val carrerasRemotos =
                carreraRepository.listarCarreras() // suspende y espera la respuesta
            adapter.actualizarLista(carrerasRemotos.toMutableList())
        }
    }

    private fun filtrarCarreras(query: String?) {
        lifecycleScope.launch {
            val carreras = carreraRepository.listarCarreras()
            val resultados = if (!query.isNullOrBlank()) {
                carreras.filter {
                    it.nombre.contains(query, true) ||
                            it.codigoCarrera.contains(query, true)
                }
            } else {
                carreras
            }
            adapter.actualizarLista(resultados.toMutableList())
        }
    }

    override fun onCarreraClick(carrera: Carrera) {
        mostrarDialogCarrera(carrera)
    }

    override fun onCarreraLongClick(carrera: Carrera): Boolean {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar carrera")
            .setMessage("¿Estás seguro de eliminar la carrera ${carrera.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarCarrera(carrera)
            }
            .setNegativeButton("Cancelar", null)
            .show()
        return true
    }

    private fun eliminarCarrera(carrera: Carrera) {
        lifecycleScope.launch {
            val carreras = carreraRepository.listarCarreras().toMutableList()
            carreras.remove(carrera)
            adapter.actualizarLista(carreras)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Carrera eliminada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarDialogCarrera(carrera: Carrera?) {
        val dialogBinding = DialogCarreraBinding.inflate(layoutInflater)
        carrera.let {
            dialogBinding.etCodigoCarrera.setText(it?.codigoCarrera)
            dialogBinding.etNombreCarrera.setText(it?.nombre)
            dialogBinding.etTituloCarrera.setText(it?.titulo)
        }

        AlertDialog.Builder(requireContext())
            .setTitle(if (carrera == null) "Agregar Carrera" else "Editar Carrera")
            .setView(dialogBinding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val cedula = dialogBinding.etCodigoCarrera.text.toString().trim()
                val nombre = dialogBinding.etNombreCarrera.text.toString().trim()
                val telefono = dialogBinding.etTituloCarrera.text.toString().trim()

                lifecycleScope.launch {
                    if (carrera == null) {
                        val nuevoCarrera = Carrera(cedula, nombre, telefono)
                        try {
                            val exito = carreraRepository.agregarCarrera(nuevoCarrera)
                            if (exito) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Carrera agregada",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    cargarCarreras()
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    } else {
                        val carrerasActualizados = carreraRepository.listarCarreras().map {
                            if (it.codigoCarrera == carrera.codigoCarrera) Carrera(
                                cedula,
                                nombre,
                                telefono
                            ) else it
                        }
                        carreraRepository.setCarreras(carrerasActualizados)
                        withContext(Dispatchers.Main) {
                            adapter.actualizarLista(carrerasActualizados.toMutableList())
                            Toast.makeText(
                                requireContext(),
                                "Carrera actualizado",
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
