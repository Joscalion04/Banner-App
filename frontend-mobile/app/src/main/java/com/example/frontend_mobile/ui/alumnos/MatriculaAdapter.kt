package com.example.frontend_mobile.ui.alumnos

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend_mobile.data.model.HistorialItem
import com.example.frontend_mobile.data.model.Matricula
import com.example.frontend_mobile.data.repository.MatriculaRepository
import com.example.frontend_mobile.databinding.ItemMatriculaCardBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MatriculaAdapter(
    internal var historial: MutableList<HistorialItem>,
    private val listener: OnMatriculaClickListener,
    private val matriculaRepository: MatriculaRepository
) : RecyclerView.Adapter<MatriculaAdapter.MatriculaViewHolder>() {

    inner class MatriculaViewHolder(val binding: ItemMatriculaCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(matriculaItem: HistorialItem) {
            binding.tvCarreraMatricula.text = "Carrera: ${matriculaItem.nombreCarrera}"
            binding.tvCicloMatricula.text = "Ciclo: ${matriculaItem.numeroCiclo} - ${matriculaItem.anioCiclo}"
            binding.tvCursoMatricula.text = "Curso: ${matriculaItem.nombreCurso}"
            binding.tvGrupoMatricula.text = "Grupo: ${matriculaItem.numeroGrupo}"
            binding.tvNotaMatricula.text = "Nota: ${matriculaItem.nota}"

            binding.root.setOnLongClickListener { listener.onMatriculaLongClick(matriculaItem) }
        }
    }

    interface OnMatriculaClickListener {
        fun onMatriculaLongClick(matriculaItem: HistorialItem): Boolean
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatriculaViewHolder {
        val binding = ItemMatriculaCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MatriculaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MatriculaViewHolder, position: Int) {
        holder.bind(historial[position])
    }

    override fun getItemCount(): Int = historial.size

    fun actualizarLista(nuevaLista: List<HistorialItem>) {
        historial.clear()
        historial.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun eliminarMatricula(pos: Int): Boolean {
        val matricula = historial[pos]
        val exito = matriculaRepository.eliminarMatricula(Matricula(matricula.matriculaId, matricula.grupoId, matricula.cedulaAlumno, 0.0))  // suspend
        if (exito) {
            historial.removeAt(pos)
            withContext(Dispatchers.Main) {
                notifyItemRemoved(pos)
            }
        }
        return exito
    }
}