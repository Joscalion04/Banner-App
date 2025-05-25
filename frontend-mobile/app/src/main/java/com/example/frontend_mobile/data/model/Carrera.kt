package com.example.frontend_mobile.data.model

data class Carrera(
    val codigoCarrera: String,
    val nombre: String,
    val titulo: String
) {
    override fun toString(): String {
        return nombre
    }
}
