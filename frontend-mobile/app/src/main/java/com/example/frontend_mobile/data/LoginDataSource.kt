package com.example.frontend_mobile.data

import com.example.frontend_mobile.data.model.Usuario
import com.example.frontend_mobile.data.repository.UsuarioRepository

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    private val usuarioRepository = UsuarioRepository

    suspend fun login(username: String, password: String): Usuario? {
        return usuarioRepository.getUsuarioRemoto(username, password)
    }

    fun logout() {
        // TODO: Implementar lógica si es necesario
    }
}