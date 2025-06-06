package com.example.frontend_mobile.ui.grupos

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend_mobile.data.model.HistorialItem
import com.example.frontend_mobile.data.repository.AlumnoRepository
import com.example.frontend_mobile.databinding.ItemHistorialCardBinding
import com.example.frontend_mobile.databinding.ItemNotasCardBinding
import com.example.frontend_mobile.ui.alumnos.MatriculaAdapter.OnMatriculaClickListener

class NotasAdapter(
    private var listaHistorial: MutableList<HistorialItem>,
    private val listener: OnNotaClickListener
) : RecyclerView.Adapter<NotasAdapter.NotasViewHolder>() {

    inner class NotasViewHolder(val binding: ItemNotasCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
         fun bind(historialItem: HistorialItem) {
             binding.tvNombreAlumno.text = "Alumno: ${historialItem.nombreAlumno}"
             binding.tvCedulaAlumno.text = "Cédula: ${historialItem.cedulaAlumno}"
             binding.tvNotaAlumno.text = "Nota: ${historialItem.nota}"

             binding.root.setOnClickListener { listener.onNotaClick(historialItem) }
        }
    }

    interface OnNotaClickListener {
        fun onNotaClick(historialItem: HistorialItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotasViewHolder {
        val binding = ItemNotasCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotasViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotasViewHolder, position: Int) {
        holder.bind(listaHistorial[position])
    }

    override fun getItemCount(): Int = listaHistorial.size

    fun actualizarLista(nuevaLista: List<HistorialItem>) {
        listaHistorial.clear()
        listaHistorial.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}