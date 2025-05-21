package com.example.frontend_mobile.ui.cursos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend_mobile.data.model.Curso
import com.example.frontend_mobile.data.repository.CursoRepository
import com.example.frontend_mobile.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CursoAdapter(
    internal var cursos: MutableList<Curso>,
    private val listener: OnCursoClickListener,
    private val repo: CursoRepository
) : RecyclerView.Adapter<CursoAdapter.CursoViewHolder>() {

    interface OnCursoClickListener {
        fun onCursoClick(curso: Curso)
        fun onCursoLongClick(curso: Curso): Boolean
    }

    inner class CursoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombre = itemView.findViewById<TextView>(R.id.tvNombreCurso)
        private val codigo = itemView.findViewById<TextView>(R.id.tvCodigoCurso)
        private val detalles = itemView.findViewById<TextView>(R.id.tvDetallesCurso)

        fun bind(c: Curso) {
            nombre.text = c.nombre
            codigo.text = "Código: ${c.codigoCurso}"
            detalles.text = "${c.creditos} créditos · ${c.horasSemanales} hrs/semana"

            itemView.setOnClickListener { listener.onCursoClick(c) }
            itemView.setOnLongClickListener { listener.onCursoLongClick(c) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CursoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_curso_card, parent, false)
        return CursoViewHolder(view)
    }

    override fun getItemCount(): Int = cursos.size

    override fun onBindViewHolder(holder: CursoViewHolder, position: Int) {
        holder.bind(cursos[position])
    }

    fun actualizarLista(nueva: List<Curso>) {
        cursos.clear()
        cursos.addAll(nueva)
        notifyDataSetChanged()
    }

    suspend fun eliminarCurso(pos: Int): Boolean {
        val curso = cursos[pos]
        val exito = repo.eliminarCurso(curso)  // suspend
        if (exito) {
            cursos.removeAt(pos)
            withContext(Dispatchers.Main) {
                notifyItemRemoved(pos)
            }
        }
        return exito
    }
}
