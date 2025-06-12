package com.example.frontend_mobile.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carreras_cursos")
data class CarreraCurso(
    @PrimaryKey(autoGenerate = true) val carreraCursoId: Int?,
    val codigoCarrera: String,
    val codigoCurso: String,
    val anio: Int,
    val ciclo: Int?,
    val orden: Int
)
