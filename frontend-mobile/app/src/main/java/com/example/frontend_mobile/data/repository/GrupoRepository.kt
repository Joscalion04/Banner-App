package com.example.frontend_mobile.data.repository

import com.example.frontend_mobile.data.model.Grupo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.mutableListOf

object GrupoRepository {
    private const val BASE_URL = "http://10.0.2.2:8080/api"
    private val gson = Gson()

    suspend fun agregarGrupoRemoto(grupo: Grupo): Boolean = withContext(Dispatchers.IO) {
        val url = URL("$BASE_URL/insertarGrupo")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.doOutput = true
        conn.setRequestProperty("Content-Type", "application/json")
        conn.setRequestProperty("Accept", "application/json")

        try {
            // Convertir el grupo a JSON (usamos Gson)
            val jsonGrupo = gson.toJson(grupo)

            // Enviar JSON
            conn.outputStream.use { os ->
                os.write(jsonGrupo.toByteArray(Charsets.UTF_8))
            }

            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                true
            } else {
                val error =
                    conn.errorStream?.bufferedReader()?.use { it.readText() } ?: "Error desconocido"
                println("Error al agregar grupo ($responseCode): $error")
                false
            }
        } catch (e: Exception) {
            println("Error en la conexión al agregar grupo: ${e.message}")
            false
        } finally {
            conn.disconnect()
        }
    }

    suspend fun listarGrupos(): List<Grupo> = withContext(Dispatchers.IO) {
        val url = URL("$BASE_URL/obtenerGrupos")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Accept", "application/json")

        try {
            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = conn.inputStream.bufferedReader().use { it.readText() }
                // Convertimos el JSON en lista de grupos con Gson
                val tipoLista = object : TypeToken<List<Grupo>>() {}.type
                gson.fromJson<List<Grupo>>(response, tipoLista)
            } else {
                val error =
                    conn.errorStream?.bufferedReader()?.use { it.readText() } ?: "Error desconocido"
                println("Error al obtener grupos ($responseCode): $error")
                emptyList()
            }
        } catch (e: Exception) {
            println("Error en la conexión al obtener grupos: ${e.message}")
            emptyList()
        } finally {
            conn.disconnect()
        }
    }

    suspend fun setGrupos(grupos: List<Grupo>): Boolean = withContext(Dispatchers.IO) {
        try {
            grupos.forEach { grupo ->
                val url = URL("$BASE_URL/actualizarGrupo")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json")
                conn.setRequestProperty("Accept", "application/json")

                val jsonGrupo = gson.toJson(grupo)

                conn.outputStream.use { os ->
                    os.write(jsonGrupo.toByteArray(Charsets.UTF_8))
                }

                val responseCode = conn.responseCode
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    val error = conn.errorStream?.bufferedReader()?.use { it.readText() }
                        ?: "Error desconocido"
                    println("Error al actualizar grupo ${grupo.grupoId} ($responseCode): $error")
                    conn.disconnect()
                    return@withContext false // falla la actualización
                }
                conn.disconnect()
            }
            true // todos actualizados con éxito
        } catch (e: Exception) {
            println("Error en la conexión al actualizar grupos: ${e.message}")
            false
        }
    }

    suspend fun eliminarGrupo(grupo: Grupo): Boolean = withContext(Dispatchers.IO) {
        val url = URL("$BASE_URL/eliminarGrupo/${grupo.grupoId}")
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
                println("Error al eliminar grupo ${grupo.grupoId} ($responseCode): $error")
                false
            }
        } catch (e: Exception) {
            println("Error en la conexión al eliminar grupo: ${e.message}")
            false
        } finally {
            conn.disconnect()
        }
    }
}
