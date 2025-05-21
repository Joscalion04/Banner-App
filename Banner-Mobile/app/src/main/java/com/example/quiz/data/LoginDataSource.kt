package com.example.quiz.data

import com.example.quiz.data.model.Usuario
import com.example.quiz.data.repository.UsuarioRepository

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