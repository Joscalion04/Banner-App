/*
    * @author: Derek Rojas Mendoza
    * @author: Joseph León Cabezas
*/


package com.example.lab4.model

import jakarta.persistence.*

@Entity
@Table(name = "CARRERA_CURSO", schema = "MOVILES")
class CarreraCurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CARRERA_CURSO_ID")
    private var carreraCursoId: Int = 0

    @Column(name = "CODIGO_CARRERA", length = 20, nullable = false)
    private var codigoCarrera: String = ""

    @Column(name = "CODIGO_CURSO", length = 20, nullable = false)
    private var codigoCurso: String = ""

    @Column(name = "ANIO", nullable = false)
    private var anio: Int = 0

    @Column(name = "CICLO", nullable = false)
    private var ciclo: Int = 0

    @Column(name = "ORDEN", nullable = false)
    private var orden: Int = 0

    constructor() {}

    constructor(carreraCursoId: Int, codigoCarrera: String, codigoCurso: String, anio: Int, ciclo: Int, orden: Int) {
        this.carreraCursoId = carreraCursoId
        this.codigoCarrera = codigoCarrera
        this.codigoCurso = codigoCurso
        this.anio = anio
        this.ciclo = ciclo
        this.orden = orden
    }

    // Getters y Setters
    fun getCarreraCursoId(): Int {
        return carreraCursoId
    }

    fun setCarreraCursoId(carreraCursoId: Int) {
        this.carreraCursoId = carreraCursoId
    }

    fun getCodigoCarrera(): String {
        return codigoCarrera
    }

    fun setCodigoCarrera(codigoCarrera: String) {
        this.codigoCarrera = codigoCarrera
    }

    fun getCodigoCurso(): String {
        return codigoCurso
    }

    fun setCodigoCurso(codigoCurso: String) {
        this.codigoCurso = codigoCurso
    }

    fun getAnio(): Int {
        return anio
    }

    fun setAnio(anio: Int) {
        this.anio = anio
    }

    fun getCiclo(): Int {
        return ciclo
    }

    fun setCiclo(ciclo: Int) {
        this.ciclo = ciclo
    }

    fun getOrden(): Int {
        return orden
    }

    fun setOrden(orden: Int) {
        this.orden = orden
    }

    // toString
    override fun toString(): String {
        return "CarreraCurso(carreraCursoId=$carreraCursoId, codigoCarrera='$codigoCarrera', codigoCurso='$codigoCurso', anio=$anio, ciclo=$ciclo, orden=$orden)"
    }
}