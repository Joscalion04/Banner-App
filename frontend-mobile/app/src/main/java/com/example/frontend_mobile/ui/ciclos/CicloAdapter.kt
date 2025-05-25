package com.example.frontend_mobile.ui.ciclos

import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend_mobile.data.model.Ciclo
import com.example.frontend_mobile.data.repository.CicloRepository
import com.example.frontend_mobile.databinding.ItemCicloCardBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class CicloAdapter(
    internal var listaCiclos: MutableList<Ciclo>,
    private val listener: OnCicloClickListener,
    private val cicloRepository: CicloRepository
) : RecyclerView.Adapter<CicloAdapter.CicloViewHolder>() {

    interface OnCicloClickListener {
        fun onCicloClick(ciclo: Ciclo)
        fun onCicloLongClick(ciclo: Ciclo): Boolean
    }

    inner class CicloViewHolder(val binding: ItemCicloCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ciclo: Ciclo) {
            binding.tvCicloID.text = "ID: " + ciclo.cicloId
            binding.tvAnioCiclo.text = "Año: " + ciclo.anio
            binding.tvNumeroCiclo.text = "Número: " + ciclo.numero
            binding.tvFechaInicioCiclo.text = Editable.Factory.getInstance().newEditable("Fecha de inicio: " + ciclo.fechaInicio)
            binding.tvFechaFinCiclo.text = Editable.Factory.getInstance().newEditable("Fecha de fin: " + ciclo.fechaFin)
            binding.tvCicloActivo.isChecked = ciclo.activo == true
            binding.root.setOnClickListener { listener.onCicloClick(ciclo) }
            binding.root.setOnLongClickListener { listener.onCicloLongClick(ciclo) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CicloViewHolder {
        val binding =
            ItemCicloCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CicloViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CicloViewHolder, position: Int) {
        holder.bind(listaCiclos[position])
    }

    override fun getItemCount(): Int = listaCiclos.size

    fun actualizarLista(nueva: List<Ciclo>) {
        listaCiclos.clear()
        listaCiclos.addAll(nueva)
        notifyDataSetChanged()
    }

    suspend fun eliminarCiclo(pos: Int): Boolean {
        val ciclo = listaCiclos[pos]
        val exito = cicloRepository.eliminarCiclo(ciclo)  // suspend
        if (exito) {
            listaCiclos.removeAt(pos)
            withContext(Dispatchers.Main) {
                notifyItemRemoved(pos)
            }
        }
        return exito
    }
}