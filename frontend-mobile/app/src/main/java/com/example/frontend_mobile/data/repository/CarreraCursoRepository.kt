package com.example.frontend_mobile.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.frontend_mobile.data.LocalDateAdapter
import com.example.frontend_mobile.data.model.CarreraCurso
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import kotlin.collections.forEach


object CarreraCursoRepository {
    private const val BASE_URL = "http://10.0.2.2:8080/api"
    @RequiresApi(Build.VERSION_CODES.O)
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun agregarCarreraCurso(carreraCurso: CarreraCurso): Boolean = withContext(Dispatchers.IO) {
        val url = URL("${BASE_URL}/insertarCarreraCurso")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.doOutput = true
        conn.setRequestProperty("Content-Type", "application/json")
        conn.setRequestProperty("Accept", "application/json")

        try {
            // Convertir el carreraCurso a JSON (usamos Gson)
            val jsonCarreraCurso = gson.toJson(carreraCurso)

            // Enviar JSON
            conn.outputStream.use { os ->
                os.write(jsonCarreraCurso.toByteArray(Charsets.UTF_8))
            }

            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                true
            } else {
                val error =
                    conn.errorStream?.bufferedReader()?.use { it.readText() } ?: "Error desconocido"
                println("Error al agregar carreraCurso ($responseCode): $error")
                false
            }
        } catch (e: Exception) {
            println("Error en la conexión al agregar carreraCurso: ${e.message}")
            false
        } finally {
            conn.disconnect()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun listarCarrerasCursos(): List<CarreraCurso> = withContext(Dispatchers.IO) {
        val url = URL("${BASE_URL}/obtenerCarrerasCursos")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Accept", "application/json")

        try {
            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = conn.inputStream.bufferedReader().use { it.readText() }
                // Convertimos el JSON en lista de carrerasCursos con Gson
                val tipoLista = object : TypeToken<List<CarreraCurso>>() {}.type
                gson.fromJson<List<CarreraCurso>>(response, tipoLista)
            } else {
                val error =
                    conn.errorStream?.bufferedReader()?.use { it.readText() } ?: "Error desconocido"
                println("Error al obtener carrerasCursos ($responseCode): $error")
                emptyList()
            }
        } catch (e: Exception) {
            println("Error en la conexión al obtener carrerasCursos: ${e.message}")
            emptyList()
        } finally {
            conn.disconnect()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun setCarrerasCursos(carrerasCursos: List<CarreraCurso>): Boolean = withContext(Dispatchers.IO) {
        try {
            carrerasCursos.forEach { carreraCurso ->
                val url = URL("${BASE_URL}/actualizarCarreraCurso")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json")
                conn.setRequestProperty("Accept", "application/json")

                val jsonCarreraCurso = gson.toJson(carreraCurso)
                conn.outputStream.use { os ->
                    os.write(jsonCarreraCurso.toByteArray(Charsets.UTF_8))
                }

                val responseCode = conn.responseCode
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    val error = conn.errorStream?.bufferedReader()?.use { it.readText() }
                        ?: "Error desconocido"
                    println("Error al actualizar carreraCurso ${carreraCurso.carreraCursoId} ($responseCode): $error")
                    conn.disconnect()
                    return@withContext false // falla la actualización
                }
                conn.disconnect()
            }
            true // todos actualizados con éxito
        } catch (e: Exception) {
            println("Error en la conexión al actualizar carrerasCursos: ${e.message}")
            false
        }
    }

    suspend fun eliminarCarreraCurso(carreraCurso: CarreraCurso): Boolean = withContext(Dispatchers.IO) {
        val url = URL("${BASE_URL}/eliminarCarreraCurso/${carreraCurso.anio}${carreraCurso.carreraCursoId}")
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
                println("Error al eliminar carreraCurso ${carreraCurso.carreraCursoId} ($responseCode): $error")
                false
            }
        } catch (e: Exception) {
            println("Error en la conexión al eliminar carreraCurso: ${e.message}")
            false
        } finally {
            conn.disconnect()
        }
    }
}