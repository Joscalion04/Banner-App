package com.example.frontend_mobile.data.repository

import com.example.frontend_mobile.data.model.Carrera
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.forEach


object CarreraRepository {
    private const val BASE_URL = "http://10.0.2.2:8080/api"
    private val gson = Gson()

    suspend fun agregarCarrera(carrera: Carrera): Boolean = withContext(Dispatchers.IO) {
        val url = URL("${CarreraRepository.BASE_URL}/insertarCarrera")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.doOutput = true
        conn.setRequestProperty("Content-Type", "application/json")
        conn.setRequestProperty("Accept", "application/json")

        try {
            // Convertir el carrera a JSON (usamos Gson)
            val jsonCarrera = CarreraRepository.gson.toJson(carrera)

            // Enviar JSON
            conn.outputStream.use { os ->
                os.write(jsonCarrera.toByteArray(Charsets.UTF_8))
            }

            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                true
            } else {
                val error =
                    conn.errorStream?.bufferedReader()?.use { it.readText() } ?: "Error desconocido"
                println("Error al agregar carrera ($responseCode): $error")
                false
            }
        } catch (e: Exception) {
            println("Error en la conexión al agregar carrera: ${e.message}")
            false
        } finally {
            conn.disconnect()
        }
    }

    suspend fun listarCarreras(): List<Carrera> = withContext(Dispatchers.IO) {
        val url = URL("${CarreraRepository.BASE_URL}/obtenerCarreras")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Accept", "application/json")

        try {
            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = conn.inputStream.bufferedReader().use { it.readText() }
                // Convertimos el JSON en lista de carreras con Gson
                val tipoLista = object : TypeToken<List<Carrera>>() {}.type
                CarreraRepository.gson.fromJson<List<Carrera>>(response, tipoLista)
            } else {
                val error =
                    conn.errorStream?.bufferedReader()?.use { it.readText() } ?: "Error desconocido"
                println("Error al obtener carreras ($responseCode): $error")
                emptyList()
            }
        } catch (e: Exception) {
            println("Error en la conexión al obtener carreras: ${e.message}")
            emptyList()
        } finally {
            conn.disconnect()
        }
    }

    suspend fun setCarreras(carreras: List<Carrera>): Boolean = withContext(Dispatchers.IO) {
        try {
            carreras.forEach { carrera ->
                val url = URL("${CarreraRepository.BASE_URL}/actualizarCarrera")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json")
                conn.setRequestProperty("Accept", "application/json")

                val jsonCarrera = CarreraRepository.gson.toJson(carrera)

                conn.outputStream.use { os ->
                    os.write(jsonCarrera.toByteArray(Charsets.UTF_8))
                }

                val responseCode = conn.responseCode
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    val error = conn.errorStream?.bufferedReader()?.use { it.readText() }
                        ?: "Error desconocido"
                    println("Error al actualizar carrera ${carrera.codigoCarrera} ($responseCode): $error")
                    conn.disconnect()
                    return@withContext false // falla la actualización
                }
                conn.disconnect()
            }
            true // todos actualizados con éxito
        } catch (e: Exception) {
            println("Error en la conexión al actualizar carreras: ${e.message}")
            false
        }
    }

    suspend fun eliminarCarrera(carrera: Carrera): Boolean = withContext(Dispatchers.IO) {
        val url = URL("${CarreraRepository.BASE_URL}/eliminarCarrera/${carrera.codigoCarrera}")
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
                println("Error al eliminar carrera ${carrera.codigoCarrera} ($responseCode): $error")
                false
            }
        } catch (e: Exception) {
            println("Error en la conexión al eliminar carrera: ${e.message}")
            false
        } finally {
            conn.disconnect()
        }
    }
}