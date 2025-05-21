package com.example.frontend_mobile.data.repository

import com.example.frontend_mobile.data.model.Curso
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.mutableListOf

object CursoRepository {
    private const val BASE_URL = "http://10.0.2.2:8080/api"
    private val gson = Gson()

    suspend fun agregarCursoRemoto(curso: Curso): Boolean = withContext(Dispatchers.IO) {
        val url = URL("$BASE_URL/insertarCurso")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.doOutput = true
        conn.setRequestProperty("Content-Type", "application/json")
        conn.setRequestProperty("Accept", "application/json")

        try {
            // Convertir el curso a JSON (usamos Gson)
            val jsonCurso = gson.toJson(curso)

            // Enviar JSON
            conn.outputStream.use { os ->
                os.write(jsonCurso.toByteArray(Charsets.UTF_8))
            }

            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                true
            } else {
                val error =
                    conn.errorStream?.bufferedReader()?.use { it.readText() } ?: "Error desconocido"
                println("Error al agregar curso ($responseCode): $error")
                false
            }
        } catch (e: Exception) {
            println("Error en la conexión al agregar curso: ${e.message}")
            false
        } finally {
            conn.disconnect()
        }
    }

    suspend fun listarCursos(): List<Curso> = withContext(Dispatchers.IO) {
        val url = URL("$BASE_URL/obtenerCursos")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Accept", "application/json")

        try {
            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = conn.inputStream.bufferedReader().use { it.readText() }
                // Convertimos el JSON en lista de cursos con Gson
                val tipoLista = object : TypeToken<List<Curso>>() {}.type
                gson.fromJson<List<Curso>>(response, tipoLista)
            } else {
                val error =
                    conn.errorStream?.bufferedReader()?.use { it.readText() } ?: "Error desconocido"
                println("Error al obtener cursos ($responseCode): $error")
                emptyList()
            }
        } catch (e: Exception) {
            println("Error en la conexión al obtener cursos: ${e.message}")
            emptyList()
        } finally {
            conn.disconnect()
        }
    }

    suspend fun generarCodigoCurso(): String {
        val cursos = listarCursos()
        val ultimoCodigo = cursos
            .mapNotNull { it.codigoCurso.toIntOrNull() }
            .maxOrNull() ?: 0
        return (ultimoCodigo + 1).toString().padStart(4, '0')
    }

    suspend fun setCursos(cursos: List<Curso>): Boolean = withContext(Dispatchers.IO) {
        try {
            cursos.forEach { curso ->
                val url = URL("$BASE_URL/actualizarCurso")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json")
                conn.setRequestProperty("Accept", "application/json")

                val jsonCurso = gson.toJson(curso)

                conn.outputStream.use { os ->
                    os.write(jsonCurso.toByteArray(Charsets.UTF_8))
                }

                val responseCode = conn.responseCode
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    val error = conn.errorStream?.bufferedReader()?.use { it.readText() }
                        ?: "Error desconocido"
                    println("Error al actualizar curso ${curso.codigoCurso} ($responseCode): $error")
                    conn.disconnect()
                    return@withContext false // falla la actualización
                }
                conn.disconnect()
            }
            true // todos actualizados con éxito
        } catch (e: Exception) {
            println("Error en la conexión al actualizar cursos: ${e.message}")
            false
        }
    }

    suspend fun eliminarCurso(curso: Curso): Boolean = withContext(Dispatchers.IO) {
        val url = URL("$BASE_URL/eliminarCurso/${curso.codigoCurso}")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "DELETE"
        conn.setRequestProperty("Accept", "application/json")

        try {
            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                true
            } else {
                val error =
                    conn.errorStream?.bufferedReader()?.use { it.readText() } ?: "Error desconocido"
                println("Error al eliminar curso ${curso.codigoCurso} ($responseCode): $error")
                false
            }
        } catch (e: Exception) {
            println("Error en la conexión al eliminar curso: ${e.message}")
            false
        } finally {
            conn.disconnect()
        }
    }
}
