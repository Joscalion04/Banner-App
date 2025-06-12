package com.example.frontend_mobile.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carreras")
data class Carrera(
    @PrimaryKey val codigoCarrera: String,
    val nombre: String,
    val titulo: String
) {
    override fun toString(): String {
        return nombre
    }
}
