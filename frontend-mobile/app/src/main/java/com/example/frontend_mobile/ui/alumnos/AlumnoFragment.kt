package com.example.frontend_mobile.ui.alumnos

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
import com.example.frontend_mobile.data.SessionManager
import com.example.frontend_mobile.data.WebSocketManager
import com.example.frontend_mobile.data.model.Alumno
import com.example.frontend_mobile.data.model.Grupo
import com.example.frontend_mobile.data.model.HistorialItem
import com.example.frontend_mobile.data.model.MatriculaRequest
import com.example.frontend_mobile.data.repository.AlumnoRepository
import com.example.frontend_mobile.data.repository.CicloRepository
import com.example.frontend_mobile.data.repository.GrupoRepository
import com.example.frontend_mobile.data.repository.HistorialRepository
import com.example.frontend_mobile.data.repository.MatriculaRepository
import com.example.frontend_mobile.data.repository.MatriculaRepository.MatriculaException
import com.example.frontend_mobile.databinding.DialogAlumnoBinding
import com.example.frontend_mobile.databinding.DialogHistorialBinding
import com.example.frontend_mobile.databinding.DialogMatriculaBinding
import com.example.frontend_mobile.databinding.FragmentAlumnosBinding
import com.example.frontend_mobile.ui.grupos.GrupoDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AlumnoFragment : Fragment(), AlumnoAdapter.OnAlumnoClickListener,
    MatriculaAdapter.OnMatriculaClickListener,
    GrupoDialogFragment.OnGrupoSelectedListener {

    private lateinit var binding: FragmentAlumnosBinding
    private val alumnoRepository = AlumnoRepository
    private lateinit var adapter: AlumnoAdapter
    private lateinit var matriculaAdapter: MatriculaAdapter
    private lateinit var matriculaBinding: DialogMatriculaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlumnosBinding.inflate(inflater, container, false)
        matriculaBinding = DialogMatriculaBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AlumnoAdapter(mutableListOf(), this, alumnoRepository)
        binding.recyclerViewAlumnos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAlumnos.adapter = adapter

        matriculaAdapter = MatriculaAdapter(mutableListOf(), this, MatriculaRepository)

        cargarAlumnos()

        WebSocketManager.conectar { tipo, evento, id ->
            if (tipo == "alumno" && (evento == "insertar" || evento == "actualizar" || evento == "eliminar")) {
                lifecycleScope.launch(Dispatchers.Main) {
                    cargarAlumnos()
                }
            }
        }

        binding.searchViewAlumnos.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true.also { filtrarAlumnos(query) }
            override fun onQueryTextChange(newText: String?) =
                true.also { filtrarAlumnos(newText) }
        })

        binding.fabAgregarAlumno.setOnClickListener {
            mostrarDialogAlumno(null)
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
                val alumno = adapter.listaAlumnos[pos]

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Eliminar alumno")
                            .setMessage("¿Eliminar ${alumno.nombre}?")
                            .setPositiveButton("Sí") { _, _ ->
                                lifecycleScope.launch {
                                    val exito = adapter.eliminarAlumno(pos)
                                    if (exito) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Alumno eliminada",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Error al eliminar la alumno",
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
                        mostrarDialogAlumno(alumno)
                    }
                }
            }
        })
        touchHelper.attachToRecyclerView(binding.recyclerViewAlumnos)

        val matriculaTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
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

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Eliminar matricula")
                            .setMessage("¿Eliminar?")
                            .setPositiveButton("Sí") { _, _ ->
                                lifecycleScope.launch {
                                    val exito = matriculaAdapter.eliminarMatricula(pos)
                                    if (exito) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Matricula eliminada",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Error al eliminar la matricula",
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
                }
            }
        })
        matriculaTouchHelper.attachToRecyclerView(matriculaBinding.recyclerViewMatricula)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cargarAlumnos() {
        lifecycleScope.launch {
            val alumnosRemotos = if (SessionManager.user?.tipoUsuario == "ADMINISTRADOR") {
                alumnoRepository.listarAlumnos()
            } else {
                alumnoRepository.listarAlumnos().filter { it.cedula == SessionManager.user?.cedula }
            }

            adapter.actualizarLista(alumnosRemotos.toMutableList())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filtrarAlumnos(query: String?) {
        lifecycleScope.launch {
            val alumnos = alumnoRepository.listarAlumnos()
            val resultados = if (!query.isNullOrBlank()) {
                alumnos.filter {
                    it.nombre.contains(query, true) ||
                            it.cedula.contains(query, true)
                }
            } else {
                alumnos
            }
            adapter.actualizarLista(resultados.toMutableList())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onAlumnoClick(alumno: Alumno) {
        mostrarDialogAlumno(alumno)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onAlumnoLongClick(alumno: Alumno): Boolean {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar alumno")
            .setMessage("¿Estás seguro de eliminar la alumno ${alumno.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarAlumno(alumno)
            }
            .setNegativeButton("Cancelar", null)
            .show()
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun eliminarAlumno(alumno: Alumno) {
        lifecycleScope.launch {
            val alumnos = alumnoRepository.listarAlumnos().toMutableList()
            alumnos.remove(alumno)
            adapter.actualizarLista(alumnos)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Alumno eliminada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMatricularClick(alumno: Alumno) {
        mostrarDialogMatricula(alumno)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onConsultarHistorialClick(alumno: Alumno) {
        mostrarDialogHistorial(alumno)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrarDialogMatricula(alumno: Alumno) {
        val dialogBinding = DialogMatriculaBinding.inflate(layoutInflater)

        dialogBinding.recyclerViewMatricula.layoutManager = LinearLayoutManager(requireContext())
        dialogBinding.recyclerViewMatricula.adapter = matriculaAdapter

        // Actualizar el título con el nombre del alumno
        dialogBinding.tvTituloMatricula.text = "Matriculas de ${alumno.nombre}"

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Matrículas")
            .setView(dialogBinding.root)
            .setPositiveButton("Cerrar", null)
            .create()


        dialogBinding.fabMatricular.setOnClickListener {
            val grupoDialog  = GrupoDialogFragment.newInstance(alumno)
            grupoDialog .show(childFragmentManager, "GrupoDialogFragment")
        }

        // Cargar el historial del alumno
        lifecycleScope.launch {
            try {
                val historial = HistorialRepository.obtenerHistorialAlumno(alumno.cedula).filter { historialItem ->
                    val grupo = GrupoRepository.listarGrupos().find { it.grupoId == historialItem.grupoId }
                    val ciclo = CicloRepository.listarCiclos().find { it.cicloId == grupo?.cicloId }
                    ciclo?.activo == true
                }
                withContext(Dispatchers.Main) {
                    if (historial.isNotEmpty()) {
                        matriculaAdapter.actualizarLista(historial)
                    } else {
                        matriculaAdapter.actualizarLista(emptyList())
                        // Mostrar mensaje cuando no hay historial
                        Toast.makeText(
                            requireContext(),
                            "No se encontró historial para ${alumno.nombre}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Error al cargar el historial: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrarDialogHistorial(alumno: Alumno) {
        val dialogBinding = DialogHistorialBinding.inflate(layoutInflater)

        // Configurar el RecyclerView
        val historialAdapter = HistorialAdapter(mutableListOf())
        dialogBinding.recyclerViewHistorial.layoutManager = LinearLayoutManager(requireContext())
        dialogBinding.recyclerViewHistorial.adapter = historialAdapter

        // Actualizar el título con el nombre del alumno
        dialogBinding.tvTituloHistorial.text = "Historial de ${alumno.nombre}"

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Historial Académico")
            .setView(dialogBinding.root)
            .setPositiveButton("Cerrar", null)
            .create()

        // Cargar el historial del alumno
        lifecycleScope.launch {
            try {
                val historial = HistorialRepository.obtenerHistorialAlumno(alumno.cedula)
                withContext(Dispatchers.Main) {
                    if (historial.isNotEmpty()) {
                        historialAdapter.actualizarLista(historial)
                    } else {
                        // Mostrar mensaje cuando no hay historial
                        Toast.makeText(
                            requireContext(),
                            "No se encontró historial para ${alumno.nombre}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Error al cargar el historial: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrarDialogAlumno(alumno: Alumno?) {
        val dialogBinding = DialogAlumnoBinding.inflate(layoutInflater)
        alumno.let {
            dialogBinding.etCedulaAlumno.setText(it?.cedula)
            dialogBinding.etNombreAlumno.setText(it?.nombre)
            dialogBinding.etTelefonoAlumno.setText(it?.telefono)
            dialogBinding.etEmailAlumno.setText(it?.email)
            dialogBinding.etFechaNacimientoAlumno.text = Editable.Factory.getInstance().newEditable(it?.fechaNacimiento?.toString() ?: "")
            dialogBinding.etCodigoCarreraAlumno.setText(it?.codigoCarrera)
        }

        AlertDialog.Builder(requireContext())
            .setTitle(if (alumno == null) "Agregar Alumno" else "Editar Alumno")
            .setView(dialogBinding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val cedula = dialogBinding.etCedulaAlumno.text.toString().trim()
                val nombre = dialogBinding.etNombreAlumno.text.toString().trim()
                val telefono = dialogBinding.etTelefonoAlumno.text.toString().trim()
                val email = dialogBinding.etEmailAlumno.text.toString().trim()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val fechaNacimiento = LocalDate.parse(dialogBinding.etFechaNacimientoAlumno.text.toString(), formatter)
                val codigoCarrera = dialogBinding.etCodigoCarreraAlumno.text.toString().trim()

                lifecycleScope.launch {
                    if (alumno == null) {
                        val nuevoAlumno = Alumno(cedula, nombre, telefono, email, fechaNacimiento, codigoCarrera)
                        try {
                            val exito = alumnoRepository.agregarAlumno(nuevoAlumno)
                            if (exito) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Alumno agregada",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    cargarAlumnos()
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    } else {
                        val alumnosActualizados = alumnoRepository.listarAlumnos().map {
                            if (it.cedula == alumno.cedula) Alumno(
                                cedula,
                                nombre,
                                telefono,
                                email,
                                fechaNacimiento,
                                codigoCarrera
                            ) else it
                        }
                        alumnoRepository.setAlumnos(alumnosActualizados)
                        withContext(Dispatchers.Main) {
                            adapter.actualizarLista(alumnosActualizados.toMutableList())
                            Toast.makeText(
                                requireContext(),
                                "Alumno actualizado",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMatriculaLongClick(item: HistorialItem): Boolean {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar matricula")
            .setMessage("¿Estás seguro de eliminar la matricula?")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarMatricula(item)
            }
            .setNegativeButton("Cancelar", null)
            .show()
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun eliminarMatricula(matricula: HistorialItem) {
        lifecycleScope.launch {
            val request = MatriculaRequest(matricula.grupoId, matricula.cedulaAlumno)
            val exito = MatriculaRepository.eliminarMatricula(request)

            withContext(Dispatchers.Main) {
                if (exito) {
                    Toast.makeText(requireContext(), "Matrícula eliminada", Toast.LENGTH_SHORT).show()
                    val historial = HistorialRepository.obtenerHistorialAlumno(matricula.cedulaAlumno).filter { historialItem ->
                        val grupo = GrupoRepository.listarGrupos().find { it.grupoId == historialItem.grupoId }
                        val ciclo = CicloRepository.listarCiclos().find { it.cicloId == grupo?.cicloId }
                        ciclo?.activo == true
                    }
                    matriculaAdapter.actualizarLista(historial)
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onGrupoSelected(grupo: Grupo, alumno: Alumno) {
        lifecycleScope.launch {
            try {
                val request = MatriculaRequest(grupo.grupoId, alumno.cedula)
                val exito = MatriculaRepository.agregarMatricula(request)

                withContext(Dispatchers.Main) {
                    if (exito) {
                        val historial = HistorialRepository.obtenerHistorialAlumno(alumno.cedula)
                            .filter { historialItem ->
                                val grupo = GrupoRepository.listarGrupos()
                                    .find { it.grupoId == historialItem.grupoId }
                                val ciclo = CicloRepository.listarCiclos()
                                    .find { it.cicloId == grupo?.cicloId }
                                ciclo?.activo == true
                            }
                        matriculaAdapter.actualizarLista(historial)
                    } else {
                        Toast.makeText(requireContext(), "Error al matricular", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } catch (e: MatriculaException) {
                // Mostrar el mensaje que llegó dentro de la excepción
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(),
                        e.message ?: "Error desconocido",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                // En caso de otra excepción inesperada
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(),
                        "Error inesperado: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
