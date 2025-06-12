package com.example.frontend_mobile.data.repository

import android.content.Context
import com.example.frontend_mobile.data.AppDatabase
import com.example.frontend_mobile.data.dao.AlumnoDao
import com.example.frontend_mobile.data.dao.UsuarioDao
import com.example.frontend_mobile.data.model.Usuario
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.EOFException
import java.net.HttpURLConnection
import java.net.URL

object UsuarioRepository {
    private lateinit var usuarioDao: UsuarioDao

    fun init(context: Context) {
        val db = AppDatabase.getDatabase(context)
        usuarioDao = db.usuarioDao()
    }

    suspend fun registrarUsuario(usuario: Usuario): Boolean {
//        val url = URL("$BASE_URL/insertarUsuario")
//        val conn = url.openConnection() as HttpURLConnection
//        conn.requestMethod = "POST"
//        conn.doOutput = true
//        conn.setRequestProperty("Content-Type", "application/json")
//
//        // Enviar JSON
//        conn.outputStream.use { os ->
//            val json = gson.toJson(usuario)
//            os.write(json.toByteArray())
//        }
//
//        val responseCode = conn.responseCode
//        try {
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                val inputStream = conn.inputStream
//                val response = inputStream?.bufferedReader()?.use { it.readText() } ?: ""
//                println("Respuesta del servidor: $response")
//            } else {
//                val errorStream = conn.errorStream
//                val errorResponse = errorStream?.bufferedReader()?.use { it.readText() } ?: ""
//                println("Error del servidor (code $responseCode): $errorResponse")
//            }
//        } catch (e: EOFException) {
//            // No hay cuerpo o cuerpo inesperado, solo loguear y continuar
//            println("EOFException: cuerpo vacío o formato inesperado en la respuesta.")
//        }

//        return responseCode == HttpURLConnection.HTTP_OK
        return true
    }

    suspend fun getUsuarioRemoto(cedula: String, clave: String): Usuario? = withContext(Dispatchers.IO) {
        try {
            var usuario = usuarioDao.obtenerUsuario(cedula, clave)

            if (usuario == null && cedula == "admin" && clave == "admin") {
                usuario = Usuario(cedula = cedula, clave = clave, "ADMINISTRADOR")
                usuarioDao.insertarUsuario(usuario)
            }

            usuario
        } catch (e: Exception) {
            println("Error al loguear: ${e.message}")
            null
        }
    }

}