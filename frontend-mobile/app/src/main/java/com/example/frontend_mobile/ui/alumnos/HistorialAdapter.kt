package com.example.frontend_mobile.ui.alumnos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend_mobile.data.model.HistorialItem
import com.example.frontend_mobile.databinding.ItemHistorialCardBinding

class HistorialAdapter(
    private var listaHistorial: MutableList<HistorialItem>
) : RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder>() {

    inner class HistorialViewHolder(val binding: ItemHistorialCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(historialItem: HistorialItem) {
            binding.tvCarreraHistorial.text = "Carrera: ${historialItem.nombreCarrera}"
            binding.tvCicloHistorial.text = "Ciclo: ${historialItem.numeroCiclo} - ${historialItem.anioCiclo}"
            binding.tvCursoHistorial.text = "Curso: ${historialItem.nombreCurso}"
            binding.tvGrupoHistorial.text = "Grupo: ${historialItem.numeroGrupo}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialViewHolder {
        val binding = ItemHistorialCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistorialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistorialViewHolder, position: Int) {
        holder.bind(listaHistorial[position])
    }

    override fun getItemCount(): Int = listaHistorial.size

    fun actualizarLista(nuevaLista: List<HistorialItem>) {
        listaHistorial.clear()
        listaHistorial.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}