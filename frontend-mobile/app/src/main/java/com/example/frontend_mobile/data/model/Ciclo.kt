package com.example.frontend_mobile.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class Ciclo(
    val cicloId: Int?,
    val anio: Int,
    val numero: Int,
    val fechaInicio: LocalDate,
    val fechaFin: LocalDate,
    @SerializedName("isActivo")
    val activo: Boolean
) {
    override fun toString(): String {
        return "Ciclo $numero - $anio"
    }
}
