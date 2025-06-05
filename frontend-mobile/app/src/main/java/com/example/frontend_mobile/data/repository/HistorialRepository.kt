package com.example.frontend_mobile.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.frontend_mobile.data.model.Grupo
import com.example.frontend_mobile.data.model.HistorialItem
import com.example.frontend_mobile.data.model.Matricula
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object HistorialRepository {
    private const val BASE_URL = "http://10.0.2.2:8080/api"
    private val gson = Gson()
    private val grupoRepository = GrupoRepository
    private val cursoRepository = CursoRepository
    private val cicloRepository = CicloRepository
    private val carreraRepository = CarreraRepository
    private val carreraCursoRepository = CarreraCursoRepository

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun obtenerHistorialAlumno(cedulaAlumno: String): List<HistorialItem> = withContext(Dispatchers.IO) {
        val url = URL("$BASE_URL/consultarHistorialAcademico/$cedulaAlumno")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Accept", "application/json")

        try {
            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = conn.inputStream.bufferedReader().use { it.readText() }

                val tipoLista = object : TypeToken<List<Matricula>>() {}.type
                val matriculas: List<Matricula> = gson.fromJson(response, tipoLista) ?: emptyList()

                matriculas.mapNotNull { matricula ->
                    try {
                        val grupos = grupoRepository.listarGrupos()
                        val cursos = cursoRepository.listarCursos()
                        val ciclos = cicloRepository.listarCiclos()
                        val carreras = carreraRepository.listarCarreras()
                        val carreraCursos = carreraCursoRepository.listarCarrerasCursos()

                        val grupo = grupos.find { it.grupoId == matricula.grupoId } ?: return@mapNotNull null
                        val curso = cursos.find { it.codigoCurso == grupo.codigoCurso } ?: return@mapNotNull null
                        val ciclo = ciclos.find { it.cicloId == grupo.cicloId } ?: return@mapNotNull null
                        val carreraCurso = carreraCursos.find { it.codigoCurso == curso.codigoCurso } ?: return@mapNotNull null
                        val carrera = carreras.find { it.codigoCarrera == carreraCurso.codigoCarrera } ?: return@mapNotNull null


                        HistorialItem(
                            nombreCarrera = carrera.nombre,
                            numeroCiclo = ciclo.numero,
                            anioCiclo = ciclo.anio,
                            nombreCurso = curso.nombre,
                            numeroGrupo = grupo.numeroGrupo,
                            matricula.grupoId,
                            matricula.cedulaAlumno,
                            matricula.nota
                        )
                    } catch (e: Exception) {
                        println("Error procesando matrícula ${matricula.matriculaId}: ${e.message}")
                        null
                    }
                }
            } else {
                val error = conn.errorStream?.bufferedReader()?.use { it.readText() } ?: "Error desconocido"
                println("Error al obtener historial ($responseCode): $error")
                emptyList()
            }
        } catch (e: Exception) {
            println("Error en la conexión al obtener historial: ${e.message}")
            emptyList()
        } finally {
            conn.disconnect()
        }
    }
}