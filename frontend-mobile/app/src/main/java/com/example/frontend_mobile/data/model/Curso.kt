package com.example.frontend_mobile.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cursos")
data class Curso(
    @PrimaryKey val codigoCurso: String,
    val nombre: String,
    val creditos: Int,
    val horasSemanales: Int
) {
    override fun toString(): String {
        return nombre
    }
}
