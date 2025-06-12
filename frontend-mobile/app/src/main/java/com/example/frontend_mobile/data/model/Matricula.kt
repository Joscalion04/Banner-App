package com.example.frontend_mobile.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matriculas")
data class Matricula(
    @PrimaryKey(autoGenerate = true) val matriculaId: Int?,
    val grupoId: Int,
    val cedulaAlumno: String,
    val nota: Double
)