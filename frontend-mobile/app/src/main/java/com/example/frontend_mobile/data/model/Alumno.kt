package com.example.frontend_mobile.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "alumnos")
data class Alumno(
    @PrimaryKey val cedula: String,
    val nombre: String,
    val telefono: String,
    val email: String,
    val fechaNacimiento: LocalDate,
    val codigoCarrera: String
)
