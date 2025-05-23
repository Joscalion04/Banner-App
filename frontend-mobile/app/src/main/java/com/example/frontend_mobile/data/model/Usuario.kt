package com.example.frontend_mobile.data.model

data class Usuario(
    val cedula: String,
    val clave: String,
    val tipoUsuario: String? = null,
)
