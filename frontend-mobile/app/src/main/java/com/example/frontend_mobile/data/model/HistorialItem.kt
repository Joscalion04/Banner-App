package com.example.frontend_mobile.data.model

data class HistorialItem(
    val nombreCarrera: String,
    val numeroCiclo: Int,
    val anioCiclo: Int,
    val nombreCurso: String,
    val numeroGrupo: Int,
    val grupoId: Int,
    val cedulaAlumno: String,
    val nota: Double,
    val nombreAlumno: String,
    val matriculaId: Int?,
)