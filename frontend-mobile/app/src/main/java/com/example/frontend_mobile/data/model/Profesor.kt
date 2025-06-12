package com.example.frontend_mobile.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profesores")
data class Profesor(
    @PrimaryKey val cedula: String,
    val nombre: String,
    val telefono: String,
    val email: String
)
