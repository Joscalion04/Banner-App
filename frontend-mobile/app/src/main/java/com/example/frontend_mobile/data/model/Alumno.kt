package com.example.frontend_mobile.data.model

import java.time.LocalDate

data class Alumno(
    val cedula: String,
    val nombre: String,
    val telefono: String,
    val email: String,
    val fechaNacimiento: LocalDate,
    val codigoCarrera: String
)
