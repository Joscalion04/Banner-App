/*
    * @author: Derek Rojas Mendoza
    * @author: Joseph León Cabezas
*/


package com.example.backend.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.time.LocalDate
import java.util.Date

@Entity
@Table(name = "CICLO", schema = "MOVILES")
class Ciclo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CICLO_ID")
    private var cicloId: Int = 0

    @Column(name = "ANIO", nullable = false)
    private var anio: Int = 0

    @Column(name = "NUMERO", nullable = false)
    private var numero: Int = 0

    @Column(name = "FECHA_INICIO", nullable = false)
    private var fechaInicio: LocalDate = LocalDate.now()

    @Column(name = "FECHA_FIN", nullable = false)
    private var fechaFin: LocalDate = LocalDate.now()

    @Column(name = "ACTIVO", nullable = false)
    @JsonProperty("isActivo")
    private var activo: Boolean = false

    constructor() {}

    constructor(cicloId: Int, anio: Int, numero: Int, fechaInicio: LocalDate, fechaFin: LocalDate, activo: Boolean = false) {
        this.cicloId = cicloId
        this.anio = anio
        this.numero = numero
        this.fechaInicio = fechaInicio
        this.fechaFin = fechaFin
        this.activo = activo
    }

    // Getters y Setters
    fun getCicloId(): Int {
        return cicloId
    }

    fun setCicloId(cicloId: Int) {
        this.cicloId = cicloId
    }

    fun getAnio(): Int {
        return anio
    }

    fun setAnio(anio: Int) {
        this.anio = anio
    }

    fun getNumero(): Int {
        return numero
    }

    fun setNumero(numero: Int) {
        if (numero == 1 || numero == 2) {
            this.numero = numero
        } else {
            throw IllegalArgumentException("El número de ciclo debe ser 1 o 2")
        }
    }

    fun getFechaInicio(): LocalDate {
        return fechaInicio
    }

    fun setFechaInicio(fechaInicio: LocalDate) {
        this.fechaInicio = fechaInicio
    }

    fun getFechaFin(): LocalDate {
        return fechaFin
    }

    fun setFechaFin(fechaFin: LocalDate) {
        this.fechaFin = fechaFin
    }

    fun isActivo(): Boolean {
        return activo
    }

    fun setActivo(activo: Boolean) {
        this.activo = activo
    }

    // toString
    override fun toString(): String {
        return "Ciclo(cicloId=$cicloId, anio=$anio, numero=$numero, fechaInicio=$fechaInicio, fechaFin=$fechaFin, activo=$activo)"
    }
}