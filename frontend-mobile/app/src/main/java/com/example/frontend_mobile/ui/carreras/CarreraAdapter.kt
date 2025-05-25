package com.example.frontend_mobile.ui.carreras

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend_mobile.data.model.Carrera
import com.example.frontend_mobile.data.repository.CarreraRepository
import com.example.frontend_mobile.databinding.ItemCarreraCardBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class CarreraAdapter(
    internal var listaCarreras: MutableList<Carrera>,
    private val listener: OnCarreraClickListener,
    private val carreraRepository: CarreraRepository
) : RecyclerView.Adapter<CarreraAdapter.CarreraViewHolder>() {

    interface OnCarreraClickListener {
        fun onCarreraClick(carrera: Carrera)
        fun onCarreraLongClick(carrera: Carrera): Boolean
    }

    inner class CarreraViewHolder(val binding: ItemCarreraCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(carrera: Carrera) {
            binding.tvCodigoCarrera.text = "Código: " + carrera.codigoCarrera
            binding.tvNombreCarrera.text = "Nombre: " + carrera.nombre
            binding.tvTituloCarrera.text = "Titulo a otorgar: " + carrera.titulo
            binding.root.setOnClickListener { listener.onCarreraClick(carrera) }
            binding.root.setOnLongClickListener { listener.onCarreraLongClick(carrera) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarreraViewHolder {
        val binding =
            ItemCarreraCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarreraViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarreraViewHolder, position: Int) {
        holder.bind(listaCarreras[position])
    }

    override fun getItemCount(): Int = listaCarreras.size

    fun actualizarLista(nueva: List<Carrera>) {
        listaCarreras.clear()
        listaCarreras.addAll(nueva)
        notifyDataSetChanged()
    }

    suspend fun eliminarCarrera(pos: Int): Boolean {
        val carrera = listaCarreras[pos]
        val exito = carreraRepository.eliminarCarrera(carrera)  // suspend
        if (exito) {
            listaCarreras.removeAt(pos)
            withContext(Dispatchers.Main) {
                notifyItemRemoved(pos)
            }
        }
        return exito
    }
}