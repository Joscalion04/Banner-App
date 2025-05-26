package com.example.frontend_mobile.data.model

data class Curso(
    val codigoCurso: String,
    val nombre: String,
    val creditos: Int,
    val horasSemanales: Int
) {
    override fun toString(): String {
        return nombre
    }
}
