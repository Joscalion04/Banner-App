package com.example.frontend_mobile.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

@Entity(tableName = "ciclos")
data class Ciclo(
    @PrimaryKey(autoGenerate = true) val cicloId: Int?,
    val anio: Int,
    val numero: Int,
    val fechaInicio: LocalDate,
    val fechaFin: LocalDate,
    @SerializedName("isActivo")
    var activo: Boolean
) {
    override fun toString(): String {
        return "Ciclo $numero - $anio"
    }
}
