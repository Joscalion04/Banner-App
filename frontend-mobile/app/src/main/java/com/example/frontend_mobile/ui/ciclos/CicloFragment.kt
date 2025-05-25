package com.example.frontend_mobile.ui.ciclos

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
import com.example.frontend_mobile.data.model.Ciclo
import com.example.frontend_mobile.data.repository.CicloRepository
import com.example.frontend_mobile.databinding.DialogCicloBinding
import com.example.frontend_mobile.databinding.FragmentCiclosBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CicloFragment : Fragment(), CicloAdapter.OnCicloClickListener {

    private lateinit var binding: FragmentCiclosBinding
    private val cicloRepository = CicloRepository
    private lateinit var adapter: CicloAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCiclosBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CicloAdapter(mutableListOf(), this, cicloRepository)
        binding.recyclerViewCiclos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCiclos.adapter = adapter

        cargarCiclos()

        WebSocketManager.conectar { tipo, evento, id ->
            if (tipo == "ciclo" && (evento == "insertar" || evento == "actualizar" || evento == "eliminar")) {
                lifecycleScope.launch(Dispatchers.Main) {
                    cargarCiclos()
                }
            }
        }

        binding.searchViewCiclos.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true.also { filtrarCiclos(query) }
            override fun onQueryTextChange(newText: String?) =
                true.also { filtrarCiclos(newText) }
        })

        binding.fabAgregarCiclo.setOnClickListener {
            mostrarDialogCiclo(null)
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
                val ciclo = adapter.listaCiclos[pos]

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Eliminar ciclo")
                            .setMessage("¿Eliminar ${ciclo.anio}/${ciclo.numero}?")
                            .setPositiveButton("Sí") { _, _ ->
                                lifecycleScope.launch {
                                    val exito = adapter.eliminarCiclo(pos)
                                    if (exito) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Ciclo eliminado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Error al eliminar el ciclo",
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
                        mostrarDialogCiclo(ciclo)
                    }
                }
            }
        })
        touchHelper.attachToRecyclerView(binding.recyclerViewCiclos)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cargarCiclos() {
        lifecycleScope.launch {
            val ciclosRemotos =
                cicloRepository.listarCiclos() // suspende y espera la respuesta
            adapter.actualizarLista(ciclosRemotos.toMutableList())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filtrarCiclos(query: String?) {
        lifecycleScope.launch {
            val ciclos = cicloRepository.listarCiclos()
            val resultados = if (!query.isNullOrBlank()) {
                ciclos.filter {
                    it.anio.toString().contains(query, true) ||
                            it.numero.toString().contains(query, true)
                }
            } else {
                ciclos
            }
            adapter.actualizarLista(resultados.toMutableList())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCicloClick(ciclo: Ciclo) {
        mostrarDialogCiclo(ciclo)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCicloLongClick(ciclo: Ciclo): Boolean {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar ciclo")
            .setMessage("¿Estás seguro de eliminar el ciclo ${ciclo.anio}/${ciclo.numero}?")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarCiclo(ciclo)
            }
            .setNegativeButton("Cancelar", null)
            .show()
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun eliminarCiclo(ciclo: Ciclo) {
        lifecycleScope.launch {
            val ciclos = cicloRepository.listarCiclos().toMutableList()
            ciclos.remove(ciclo)
            adapter.actualizarLista(ciclos)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Ciclo eliminado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrarDialogCiclo(ciclo: Ciclo?) {
        val dialogBinding = DialogCicloBinding.inflate(layoutInflater)
        ciclo.let {
            dialogBinding.etCicloID.text = it?.cicloId.toString()
            dialogBinding.etAnioCiclo.setText(it?.anio?.toString() ?: "")
            dialogBinding.etNumeroCiclo.setText(it?.numero?.toString() ?: "")
            dialogBinding.etFechaInicioCiclo.text = Editable.Factory.getInstance().newEditable(it?.fechaInicio?.toString() ?: "")
            dialogBinding.etFechaFinCiclo.text = Editable.Factory.getInstance().newEditable(it?.fechaFin?.toString() ?: "")
            dialogBinding.etCicloActivo.isChecked = it?.activo == true
        }

        AlertDialog.Builder(requireContext())
            .setTitle(if (ciclo == null) "Agregar Ciclo" else "Editar Ciclo")
            .setView(dialogBinding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val cicloId = dialogBinding.etCicloID.text.toString().toIntOrNull()
                val anio = dialogBinding.etAnioCiclo.text.toString().toInt()
                val numero = dialogBinding.etNumeroCiclo.text.toString().toInt()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val fechaInicio = LocalDate.parse(dialogBinding.etFechaInicioCiclo.text.toString(), formatter)
                val fechaFin = LocalDate.parse(dialogBinding.etFechaFinCiclo.text.toString(), formatter)
                val activo = dialogBinding.etCicloActivo.isChecked

                lifecycleScope.launch {
                    if (ciclo == null) {
                        val nuevoCiclo = Ciclo(cicloId, anio, numero, fechaInicio, fechaFin, activo)
                        try {
                            val exito = cicloRepository.agregarCiclo(nuevoCiclo)
                            if (exito) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Ciclo agregada",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    cargarCiclos()
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    } else {
                        val ciclosActualizados = cicloRepository.listarCiclos().map {
                            if (it.cicloId == ciclo.cicloId) Ciclo(
                                cicloId,
                                anio,
                                numero,
                                fechaInicio,
                                fechaFin,
                                activo
                            ) else it
                        }
                        cicloRepository.setCiclos(ciclosActualizados)
                        withContext(Dispatchers.Main) {
                            adapter.actualizarLista(ciclosActualizados.toMutableList())
                            Toast.makeText(
                                requireContext(),
                                "Ciclo actualizado",
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
