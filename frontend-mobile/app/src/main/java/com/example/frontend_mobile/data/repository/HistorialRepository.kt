package com.example.frontend_mobile.data.repository

import android.content.Context
import com.example.frontend_mobile.data.AppDatabase
import com.example.frontend_mobile.data.dao.AlumnoDao
import com.example.frontend_mobile.data.dao.CarreraCursoDao
import com.example.frontend_mobile.data.dao.CarreraDao
import com.example.frontend_mobile.data.dao.CicloDao
import com.example.frontend_mobile.data.dao.CursoDao
import com.example.frontend_mobile.data.dao.GrupoDao
import com.example.frontend_mobile.data.dao.MatriculaDao
import com.example.frontend_mobile.data.model.HistorialItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object HistorialRepository {
    private lateinit var grupoDao: GrupoDao
    private lateinit var cursoDao: CursoDao
    private lateinit var cicloDao: CicloDao
    private lateinit var carreraDao: CarreraDao
    private lateinit var carreraCursoDao: CarreraCursoDao
    private lateinit var alumnoDao: AlumnoDao
    private lateinit var matriculaDao: MatriculaDao

    fun init(context: Context) {
        val db = AppDatabase.getDatabase(context)
        grupoDao = db.grupoDao()
        cursoDao = db.cursoDao()
        cicloDao = db.cicloDao()
        carreraDao = db.carreraDao()
        carreraCursoDao = db.carreraCursoDao()
        alumnoDao = db.alumnoDao()
        matriculaDao = db.matriculaDao()
    }

    suspend fun obtenerHistorialAlumno(cedulaAlumno: String): List<HistorialItem> = withContext(Dispatchers.IO) {
        try {
            val matriculas = matriculaDao.obtenerMatriculasPorAlumno(cedulaAlumno)
            val grupos = grupoDao.obtenerGrupos()
            val cursos = cursoDao.obtenerCursos()
            val ciclos = cicloDao.obtenerCiclos()
            val carreras = carreraDao.obtenerCarreras()
            val carreraCursos = carreraCursoDao.obtenerCarreraCursos()
            val alumnos = alumnoDao.obtenerAlumnos()

            matriculas.mapNotNull { matricula ->
                try {
                    val grupo = grupos.find { it.grupoId == matricula.grupoId } ?: return@mapNotNull null
                    val curso = cursos.find { it.codigoCurso == grupo.codigoCurso } ?: return@mapNotNull null
                    val ciclo = ciclos.find { it.cicloId == grupo.cicloId } ?: return@mapNotNull null
                    val carreraCurso = carreraCursos.find { it.codigoCurso == curso.codigoCurso } ?: return@mapNotNull null
                    val carrera = carreras.find { it.codigoCarrera == carreraCurso.codigoCarrera } ?: return@mapNotNull null
                    val alumno = alumnos.find { it.cedula == matricula.cedulaAlumno } ?: return@mapNotNull null

                    HistorialItem(
                        nombreCarrera = carrera.nombre,
                        numeroCiclo = ciclo.numero,
                        anioCiclo = ciclo.anio,
                        nombreCurso = curso.nombre,
                        numeroGrupo = grupo.numeroGrupo,
                        grupoId = matricula.grupoId,
                        cedulaAlumno = matricula.cedulaAlumno,
                        nota = matricula.nota,
                        nombreAlumno = alumno?.nombre ?: "Desconocido",
                        matriculaId = matricula.matriculaId
                    )
                } catch (e: Exception) {
                    println("Error procesando matrícula ${matricula.matriculaId}: ${e.message}")
                    null
                }
            }
        } catch (e: Exception) {
            println("Error al obtener historial local: ${e.message}")
            emptyList()
        }
    }

    suspend fun obtenerMatriculasPorGrupo(grupoId: Int): List<HistorialItem> = withContext(Dispatchers.IO) {
        try {
            val matriculas = matriculaDao.obtenerMatriculasPorGrupo(grupoId)
            val grupos = grupoDao.obtenerGrupos()
            val cursos = cursoDao.obtenerCursos()
            val ciclos = cicloDao.obtenerCiclos()
            val carreras = carreraDao.obtenerCarreras()
            val carreraCursos = carreraCursoDao.obtenerCarreraCursos()
            val alumnos = alumnoDao.obtenerAlumnos()

            matriculas.mapNotNull { matricula ->
                try {
                    val grupo = grupos.find { it.grupoId == matricula.grupoId } ?: return@mapNotNull null
                    val curso = cursos.find { it.codigoCurso == grupo.codigoCurso } ?: return@mapNotNull null
                    val ciclo = ciclos.find { it.cicloId == grupo.cicloId } ?: return@mapNotNull null
                    val carreraCurso = carreraCursos.find { it.codigoCurso == curso.codigoCurso } ?: return@mapNotNull null
                    val carrera = carreras.find { it.codigoCarrera == carreraCurso.codigoCarrera } ?: return@mapNotNull null
                    val alumno = alumnos.find { it.cedula == matricula.cedulaAlumno } ?: return@mapNotNull null

                    HistorialItem(
                        nombreCarrera = carrera.nombre,
                        numeroCiclo = ciclo.numero,
                        anioCiclo = ciclo.anio,
                        nombreCurso = curso.nombre,
                        numeroGrupo = grupo.numeroGrupo,
                        grupoId = matricula.grupoId,
                        cedulaAlumno = matricula.cedulaAlumno,
                        nota = matricula.nota,
                        nombreAlumno = alumno.nombre,
                        matriculaId = matricula.matriculaId
                    )
                } catch (e: Exception) {
                    println("Error procesando matrícula ${matricula.matriculaId}: ${e.message}")
                    null
                }
            }
        } catch (e: Exception) {
            println("Error al obtener historial por grupo: ${e.message}")
            emptyList()
        }
    }
}
