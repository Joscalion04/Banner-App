package com.example.frontend_mobile.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grupos")
data class Grupo(
    @PrimaryKey(autoGenerate = true) val grupoId: Int,
    val cicloId: Int?,
    val codigoCurso: String,
    val numeroGrupo: Int,
    val horario: String,
    val cedulaProfesor: String
)
