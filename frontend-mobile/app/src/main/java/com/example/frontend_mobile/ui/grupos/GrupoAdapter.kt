package com.example.frontend_mobile.ui.grupos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend_mobile.R
import com.example.frontend_mobile.data.model.Grupo

class GrupoAdapter(
    internal var grupos: MutableList<Grupo>,
    private val listener: OnGrupoClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_TIPO_GRUPO = 1
    private val ITEM_TIPO_VACIO = 0

    interface OnGrupoClickListener {
        fun onGrupoClick(grupo: Grupo)
        fun onGrupoLongClick(grupo: Grupo): Boolean
        fun isDialogMode(): Boolean
    }

    inner class GrupoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNumeroGrupo = itemView.findViewById<TextView>(R.id.tvNumeroGrupo)
        private val tvHorario = itemView.findViewById<TextView>(R.id.tvHorario)
        private val tvProfesor = itemView.findViewById<TextView>(R.id.tvProfesor)

        fun bind(grupo: Grupo) {
            tvNumeroGrupo.text = "Grupo ${grupo.numeroGrupo}"
            tvHorario.text = grupo.horario
            tvProfesor.text = "Prof: ${grupo.cedulaProfesor}"

            itemView.setOnClickListener { listener.onGrupoClick(grupo) }
            itemView.setOnLongClickListener {
                if (listener.isDialogMode()) {
                    false // No permitir en modo diálogo
                } else {
                    listener.onGrupoLongClick(grupo)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == ITEM_TIPO_GRUPO) {
            val view = inflater.inflate(R.layout.item_grupo_card, parent, false)
            GrupoViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_grupo_card, parent, false)
            EmptyViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        // Siempre retornamos al menos 1 para mostrar el mensaje cuando no hay grupos
        return if (grupos.isEmpty()) 1 else grupos.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (grupos.isEmpty()) ITEM_TIPO_VACIO else ITEM_TIPO_GRUPO
    }

    inner class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNumeroGrupo = itemView.findViewById<TextView>(R.id.tvNumeroGrupo)

        fun bind() {
            tvNumeroGrupo.text = "No hay grupos con la configuración seleccionada"
            // Ocultar otros TextViews para que no aparezcan
            itemView.findViewById<TextView>(R.id.tvHorario).visibility = View.GONE
            itemView.findViewById<TextView>(R.id.tvProfesor).visibility = View.GONE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GrupoViewHolder) {
            holder.bind(grupos[position])
        } else if (holder is EmptyViewHolder) {
            holder.bind()
        }
    }

    fun actualizarLista(nuevosGrupos: List<Grupo>) {
        grupos.clear()
        grupos.addAll(nuevosGrupos)
        notifyDataSetChanged()
    }

    fun eliminarGrupo(position: Int) {
        grupos.removeAt(position)
        notifyItemRemoved(position)
    }
}