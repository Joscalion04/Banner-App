package com.example.frontend_mobile.data

import android.content.Context
import com.example.frontend_mobile.data.model.Usuario
import com.example.frontend_mobile.data.repository.UsuarioRepository

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(private val context: Context) {

    private val usuarioRepository = UsuarioRepository

    suspend fun login(username: String, password: String): Usuario? {
        usuarioRepository.init(context.applicationContext)
        return usuarioRepository.getUsuarioRemoto(username, password)
    }

    fun logout() {
        // TODO: Implementar lógica si es necesario
    }
}