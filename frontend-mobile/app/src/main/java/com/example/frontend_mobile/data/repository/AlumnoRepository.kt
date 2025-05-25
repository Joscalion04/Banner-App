package com.example.frontend_mobile.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.frontend_mobile.data.LocalDateAdapter
import com.example.frontend_mobile.data.model.Alumno
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import kotlin.collections.forEach


object AlumnoRepository {
    private const val BASE_URL = "http://10.0.2.2:8080/api"
    @RequiresApi(Build.VERSION_CODES.O)
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun agregarAlumno(alumno: Alumno): Boolean = withContext(Dispatchers.IO) {
        val url = URL("${BASE_URL}/insertarAlumno")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.doOutput = true
        conn.setRequestProperty("Content-Type", "application/json")
        conn.setRequestProperty("Accept", "application/json")

        try {
            // Convertir el alumno a JSON (usamos Gson)
            val jsonAlumno = gson.toJson(alumno)

            // Enviar JSON
            conn.outputStream.use { os ->
                os.write(jsonAlumno.toByteArray(Charsets.UTF_8))
            }

            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                true
            } else {
                val error =
                    conn.errorStream?.bufferedReader()?.use { it.readText() } ?: "Error desconocido"
                println("Error al agregar alumno ($responseCode): $error")
                false
            }
        } catch (e: Exception) {
            println("Error en la conexión al agregar alumno: ${e.message}")
            false
        } finally {
            conn.disconnect()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun listarAlumnos(): List<Alumno> = withContext(Dispatchers.IO) {
        val url = URL("${BASE_URL}/obtenerAlumnos")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Accept", "application/json")

        try {
            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = conn.inputStream.bufferedReader().use { it.readText() }
                // Convertimos el JSON en lista de alumnos con Gson
                val tipoLista = object : TypeToken<List<Alumno>>() {}.type
                gson.fromJson<List<Alumno>>(response, tipoLista)
            } else {
                val error =
                    conn.errorStream?.bufferedReader()?.use { it.readText() } ?: "Error desconocido"
                println("Error al obtener alumnos ($responseCode): $error")
                emptyList()
            }
        } catch (e: Exception) {
            println("Error en la conexión al obtener alumnos: ${e.message}")
            emptyList()
        } finally {
            conn.disconnect()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun setAlumnos(alumnos: List<Alumno>): Boolean = withContext(Dispatchers.IO) {
        try {
            alumnos.forEach { alumno ->
                val url = URL("${BASE_URL}/actualizarAlumno")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json")
                conn.setRequestProperty("Accept", "application/json")

                val jsonAlumno = gson.toJson(alumno)

                conn.outputStream.use { os ->
                    os.write(jsonAlumno.toByteArray(Charsets.UTF_8))
                }

                val responseCode = conn.responseCode
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    val error = conn.errorStream?.bufferedReader()?.use { it.readText() }
                        ?: "Error desconocido"
                    println("Error al actualizar alumno ${alumno.cedula} ($responseCode): $error")
                    conn.disconnect()
                    return@withContext false // falla la actualización
                }
                conn.disconnect()
            }
            true // todos actualizados con éxito
        } catch (e: Exception) {
            println("Error en la conexión al actualizar alumnos: ${e.message}")
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun eliminarAlumno(alumno: Alumno): Boolean = withContext(Dispatchers.IO) {
        val url = URL("${BASE_URL}/eliminarAlumno/${alumno.cedula}")
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
                println("Error al eliminar alumno ${alumno.cedula} ($responseCode): $error")
                false
            }
        } catch (e: Exception) {
            println("Error en la conexión al eliminar alumno: ${e.message}")
            false
        } finally {
            conn.disconnect()
        }
    }
}