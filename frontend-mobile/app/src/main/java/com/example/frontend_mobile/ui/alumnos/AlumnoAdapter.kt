package com.example.frontend_mobile.ui.alumnos

import android.os.Build
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend_mobile.data.SessionManager
import com.example.frontend_mobile.data.model.Alumno
import com.example.frontend_mobile.data.repository.AlumnoRepository
import com.example.frontend_mobile.databinding.ItemAlumnoCardBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class AlumnoAdapter(
    internal var listaAlumnos: MutableList<Alumno>,
    private val listener: OnAlumnoClickListener,
    private val alumnoRepository: AlumnoRepository
) : RecyclerView.Adapter<AlumnoAdapter.AlumnoViewHolder>() {

    inner class AlumnoViewHolder(val binding: ItemAlumnoCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(alumno: Alumno) {
            binding.tvCedulaAlumno.text = "Cédula: " + alumno.cedula
            binding.tvNombreAlumno.text = "Nombre: " + alumno.nombre
            binding.tvTelefonoAlumno.text = "Teléfono: " + alumno.telefono
            binding.tvEmailAlumno.text = "Correo: " + alumno.email
            binding.tvFechaNacimientoAlumno.text =
                Editable.Factory.getInstance().newEditable("Fecha de nacimiento: " + alumno.fechaNacimiento)
            binding.tvCodigoCarreraAlumno.text = "Carrera: " + alumno.codigoCarrera

            binding.btnMatricular.visibility = if (SessionManager.user?.tipoUsuario == "ADMINISTRADOR") View.GONE else View.VISIBLE
            binding.btnConsultarHistorial.visibility = if (SessionManager.user?.tipoUsuario == "ADMINISTRADOR") View.GONE else View.VISIBLE

            binding.root.setOnClickListener { listener.onAlumnoClick(alumno) }
            binding.root.setOnLongClickListener { listener.onAlumnoLongClick(alumno) }

            binding.btnMatricular.setOnClickListener { listener.onMatricularClick(alumno) }
            binding.btnConsultarHistorial.setOnClickListener { listener.onConsultarHistorialClick(alumno) }
        }
    }

    interface OnAlumnoClickListener {
        fun onAlumnoClick(alumno: Alumno)
        fun onAlumnoLongClick(alumno: Alumno): Boolean
        fun onMatricularClick(alumno: Alumno)
        fun onConsultarHistorialClick(alumno: Alumno)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumnoViewHolder {
        val binding =
            ItemAlumnoCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlumnoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlumnoViewHolder, position: Int) {
        holder.bind(listaAlumnos[position])
    }

    override fun getItemCount(): Int = listaAlumnos.size

    fun actualizarLista(nueva: List<Alumno>) {
        listaAlumnos.clear()
        listaAlumnos.addAll(nueva)
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun eliminarAlumno(pos: Int): Boolean {
        val alumno = listaAlumnos[pos]
        val exito = alumnoRepository.eliminarAlumno(alumno)  // suspend
        if (exito) {
            listaAlumnos.removeAt(pos)
            withContext(Dispatchers.Main) {
                notifyItemRemoved(pos)
            }
        }
        return exito
    }
}