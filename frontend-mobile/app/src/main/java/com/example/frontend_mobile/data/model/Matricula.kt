package com.example.frontend_mobile.data.model

data class Matricula(
    val matriculaId: Int,
    val anioCiclo: Int,
    val numeroCiclo: Int,
    val codigoCurso: String,
    val numeroGrupo: Int,
    val cedulaAlumno: String,
    val nota: Double
)
