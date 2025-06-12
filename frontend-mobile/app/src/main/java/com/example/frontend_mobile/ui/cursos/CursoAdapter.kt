package com.example.frontend_mobile.ui.cursos

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend_mobile.data.model.Curso
import com.example.frontend_mobile.data.repository.CursoRepository
import com.example.frontend_mobile.R
import com.example.frontend_mobile.data.model.Carrera
import com.example.frontend_mobile.data.model.CarreraCurso
import com.example.frontend_mobile.data.model.Ciclo
import com.example.frontend_mobile.data.repository.CarreraCursoRepository
import com.example.frontend_mobile.data.repository.CarreraRepository
import com.example.frontend_mobile.data.repository.CicloRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CursoAdapter(
    internal var cursos: MutableList<Curso>,
    private val listener: OnCursoClickListener,
    private val repo: CursoRepository,
    private val carreraRepository: CarreraRepository,
    private val cicloRepository: CicloRepository,
    private val carrerasCursosRepository: CarreraCursoRepository
) : RecyclerView.Adapter<CursoAdapter.CursoViewHolder>() {

    var listaCarreras: List<Carrera> = emptyList()
    var listaCiclos: List<Ciclo> = emptyList()
    var listaCarrerasCursos: List<CarreraCurso> = emptyList()

    interface OnCursoClickListener {
        fun onCursoClick(curso: Curso)
        fun onCursoLongClick(curso: Curso): Boolean
    }

    inner class CursoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombre = itemView.findViewById<TextView>(R.id.tvNombreCurso)
        private val codigo = itemView.findViewById<TextView>(R.id.tvCodigoCurso)
        private val detalles = itemView.findViewById<TextView>(R.id.tvDetallesCurso)
        private val spinnerCarrera = itemView.findViewById<Spinner>(R.id.tvSpinnerCarreraCurso)
        private val spinnerCiclo = itemView.findViewById<Spinner>(R.id.tvSpinnerCicloCurso)

        fun bind(c: Curso) {
            nombre.text = "Nombre: " + c.nombre
            codigo.text = "Código: ${c.codigoCurso}"
            detalles.text = "Detalles: " + "${c.creditos} créditos · ${c.horasSemanales} hrs/semana"

            val carreraAdapter = ArrayAdapter(itemView.context, android.R.layout.simple_spinner_item, listaCarreras)
            carreraAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCarrera.adapter = carreraAdapter

            val cicloAdapter = ArrayAdapter(itemView.context, android.R.layout.simple_spinner_item, listaCiclos)
            cicloAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCiclo.adapter = cicloAdapter

            // 1. Buscar la relación CarreraCurso correspondiente a este curso
            val relacion = listaCarrerasCursos.find { it.codigoCurso == c.codigoCurso }

            // 2. Si existe la relación
            if (relacion != null) {
                // Buscar el índice de la carrera en listaCarreras
                val indiceCarrera = listaCarreras.indexOfFirst { it.codigoCarrera == relacion.codigoCarrera }
                if (indiceCarrera != -1) {
                    spinnerCarrera.setSelection(indiceCarrera)
                } else {
                    spinnerCarrera.setSelection(0) // Default
                }

                // Buscar el índice del ciclo en listaCiclos
                val indiceCiclo = listaCiclos.indexOfFirst { it.cicloId == relacion.ciclo }
                if (indiceCiclo != -1) {
                    spinnerCiclo.setSelection(indiceCiclo)
                } else {
                    spinnerCiclo.setSelection(0) // Default
                }
            } else {
                // Si no hay relación, seleccionar la primera opción por defecto
                spinnerCarrera.setSelection(0)
                spinnerCiclo.setSelection(0)
            }

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

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun actualizarLista(nueva: List<Curso>) {
        cursos.clear()
        cursos.addAll(nueva)
        listaCarreras = carreraRepository.listarCarreras()
        listaCiclos = cicloRepository.listarCiclos()
        listaCarrerasCursos = carrerasCursosRepository.listarCarrerasCursos()
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
