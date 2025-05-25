package com.example.frontend_mobile.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.frontend_mobile.data.LocalDateAdapter
import com.example.frontend_mobile.data.model.Ciclo
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import kotlin.collections.forEach


object CicloRepository {
    private const val BASE_URL = "http://10.0.2.2:8080/api"
    @RequiresApi(Build.VERSION_CODES.O)
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun agregarCiclo(ciclo: Ciclo): Boolean = withContext(Dispatchers.IO) {
        val url = URL("${BASE_URL}/insertarCiclo")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.doOutput = true
        conn.setRequestProperty("Content-Type", "application/json")
        conn.setRequestProperty("Accept", "application/json")

        try {
            // Convertir el ciclo a JSON (usamos Gson)
            val jsonCiclo = gson.toJson(ciclo)

            // Enviar JSON
            conn.outputStream.use { os ->
                os.write(jsonCiclo.toByteArray(Charsets.UTF_8))
            }

            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                true
            } else {
                val error =
                    conn.errorStream?.bufferedReader()?.use { it.readText() } ?: "Error desconocido"
                println("Error al agregar ciclo ($responseCode): $error")
                false
            }
        } catch (e: Exception) {
            println("Error en la conexión al agregar ciclo: ${e.message}")
            false
        } finally {
            conn.disconnect()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun listarCiclos(): List<Ciclo> = withContext(Dispatchers.IO) {
        val url = URL("${BASE_URL}/obtenerCiclos")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Accept", "application/json")

        try {
            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = conn.inputStream.bufferedReader().use { it.readText() }
                // Convertimos el JSON en lista de ciclos con Gson
                val tipoLista = object : TypeToken<List<Ciclo>>() {}.type
                gson.fromJson<List<Ciclo>>(response, tipoLista)
            } else {
                val error =
                    conn.errorStream?.bufferedReader()?.use { it.readText() } ?: "Error desconocido"
                println("Error al obtener ciclos ($responseCode): $error")
                emptyList()
            }
        } catch (e: Exception) {
            println("Error en la conexión al obtener ciclos: ${e.message}")
            emptyList()
        } finally {
            conn.disconnect()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun setCiclos(ciclos: List<Ciclo>): Boolean = withContext(Dispatchers.IO) {
        try {
            ciclos.forEach { ciclo ->
                val url = URL("${BASE_URL}/actualizarCiclo")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json")
                conn.setRequestProperty("Accept", "application/json")

                val jsonCiclo = gson.toJson(ciclo)
                conn.outputStream.use { os ->
                    os.write(jsonCiclo.toByteArray(Charsets.UTF_8))
                }

                val responseCode = conn.responseCode
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    val error = conn.errorStream?.bufferedReader()?.use { it.readText() }
                        ?: "Error desconocido"
                    println("Error al actualizar ciclo ${ciclo.cicloId} ($responseCode): $error")
                    conn.disconnect()
                    return@withContext false // falla la actualización
                }
                conn.disconnect()
            }
            true // todos actualizados con éxito
        } catch (e: Exception) {
            println("Error en la conexión al actualizar ciclos: ${e.message}")
            false
        }
    }

    suspend fun eliminarCiclo(ciclo: Ciclo): Boolean = withContext(Dispatchers.IO) {
        val url = URL("${BASE_URL}/eliminarCiclo/${ciclo.anio}${ciclo.numero}")
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
                println("Error al eliminar ciclo ${ciclo.cicloId} ($responseCode): $error")
                false
            }
        } catch (e: Exception) {
            println("Error en la conexión al eliminar ciclo: ${e.message}")
            false
        } finally {
            conn.disconnect()
        }
    }
}