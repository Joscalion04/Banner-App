package com.example.frontend_mobile.data.repository

import com.example.frontend_mobile.data.model.Profesor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.forEach

object ProfesorRepository {
    private const val BASE_URL = "http://10.0.2.2:8080/api"
    private val gson = Gson()

    suspend fun agregarProfesor(profesor: Profesor): Boolean = withContext(Dispatchers.IO) {
        val url = URL("$BASE_URL/insertarProfesor")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.doOutput = true
        conn.setRequestProperty("Content-Type", "application/json")
        conn.setRequestProperty("Accept", "application/json")

        try {
            // Convertir el profesor a JSON (usamos Gson)
            val jsonProfesor = gson.toJson(profesor)

            // Enviar JSON
            conn.outputStream.use { os ->
                os.write(jsonProfesor.toByteArray(Charsets.UTF_8))
            }

            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                true
            } else {
                val error =
                    conn.errorStream?.bufferedReader()?.use { it.readText() } ?: "Error desconocido"
                println("Error al agregar profesor ($responseCode): $error")
                false
            }
        } catch (e: Exception) {
            println("Error en la conexión al agregar profesor: ${e.message}")
            false
        } finally {
            conn.disconnect()
        }
    }

    suspend fun listarProfesores(): List<Profesor> = withContext(Dispatchers.IO) {
        val url = URL("$BASE_URL/obtenerProfesores")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Accept", "application/json")

        try {
            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = conn.inputStream.bufferedReader().use { it.readText() }
                // Convertimos el JSON en lista de profesors con Gson
                val tipoLista = object : TypeToken<List<Profesor>>() {}.type
                gson.fromJson<List<Profesor>>(response, tipoLista)
            } else {
                val error =
                    conn.errorStream?.bufferedReader()?.use { it.readText() } ?: "Error desconocido"
                println("Error al obtener profesors ($responseCode): $error")
                emptyList()
            }
        } catch (e: Exception) {
            println("Error en la conexión al obtener profesors: ${e.message}")
            emptyList()
        } finally {
            conn.disconnect()
        }
    }

    suspend fun setProfesores(profesors: List<Profesor>): Boolean = withContext(Dispatchers.IO) {
        try {
            profesors.forEach { profesor ->
                val url = URL("$BASE_URL/actualizarProfesor")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json")
                conn.setRequestProperty("Accept", "application/json")

                val jsonProfesor = gson.toJson(profesor)

                conn.outputStream.use { os ->
                    os.write(jsonProfesor.toByteArray(Charsets.UTF_8))
                }

                val responseCode = conn.responseCode
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    val error = conn.errorStream?.bufferedReader()?.use { it.readText() }
                        ?: "Error desconocido"
                    println("Error al actualizar profesor ${profesor.cedula} ($responseCode): $error")
                    conn.disconnect()
                    return@withContext false // falla la actualización
                }
                conn.disconnect()
            }
            true // todos actualizados con éxito
        } catch (e: Exception) {
            println("Error en la conexión al actualizar profesors: ${e.message}")
            false
        }
    }

    suspend fun eliminarProfesor(profesor: Profesor): Boolean = withContext(Dispatchers.IO) {
        val url = URL("$BASE_URL/eliminarProfesor/${profesor.cedula}")
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
                println("Error al eliminar profesor ${profesor.cedula} ($responseCode): $error")
                false
            }
        } catch (e: Exception) {
            println("Error en la conexión al eliminar profesor: ${e.message}")
            false
        } finally {
            conn.disconnect()
        }
    }
}
