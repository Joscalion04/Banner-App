package com.example.frontend_mobile.data.repository

import com.example.frontend_mobile.data.model.MatriculaRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.forEach

object MatriculaRepository {
    private const val BASE_URL = "http://10.0.2.2:8080/api"
    private val gson = Gson()

    class MatriculaException(message: String) : Exception(message)

    suspend fun agregarMatricula(matricula: MatriculaRequest): Boolean = withContext(Dispatchers.IO) {
        val url = URL("$BASE_URL/registrarMatricula")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.doOutput = true
        conn.setRequestProperty("Content-Type", "application/json")
        conn.setRequestProperty("Accept", "application/json")

        try {
            // Convertir el matricula a JSON (usamos Gson)
            val jsonMatricula = gson.toJson(matricula)
            
            // Enviar JSON
            conn.outputStream.use { os ->
                os.write(jsonMatricula.toByteArray(Charsets.UTF_8))
            }

            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                true
            } else {
                val error = conn.errorStream
                    ?.bufferedReader()
                    ?.use { it.readText() }
                    ?: "Error desconocido"
                throw MatriculaException(error)
            }
        } catch (e: Exception) {
            throw MatriculaException(e.message.toString())
        } finally {
            conn.disconnect()
        }
    }

    suspend fun eliminarMatricula(matricula: MatriculaRequest): Boolean = withContext(Dispatchers.IO) {
        val url = URL("$BASE_URL/eliminarMatricula")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "DELETE"
        conn.doOutput = true
        conn.setRequestProperty("Content-Type", "application/json")
        conn.setRequestProperty("Accept", "application/json")

        try {
            // Convertir el matricula a JSON (usamos Gson)
            val jsonMatricula = gson.toJson(matricula)

            // Enviar JSON
            conn.outputStream.use { os ->
                os.write(jsonMatricula.toByteArray(Charsets.UTF_8))
            }

            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                true
            } else {
                val error = conn.errorStream
                    ?.bufferedReader()
                    ?.use { it.readText() }
                    ?: "Error desconocido"
                throw MatriculaException("Error $responseCode: $error")
            }
        } catch (e: Exception) {
            throw MatriculaException("Error de conexión: ${e.message}")
        } finally {
            conn.disconnect()
        }
    }

}
