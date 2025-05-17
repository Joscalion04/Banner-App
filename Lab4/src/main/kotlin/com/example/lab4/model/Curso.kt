/*
    * @author: Derek Rojas Mendoza
    * @author: Joseph León Cabezas
*/


package com.example.lab4.model

import jakarta.persistence.*

@Entity
@Table(name = "CURSO", schema = "MOVILES")
class Curso {
    @Id
    @Column(name = "CODIGO_CURSO", length = 20, nullable = false)
    private var codigoCurso: String = ""

    @Column(name = "NOMBRE", length = 100, nullable = false)
    private var nombre: String = ""

    @Column(name = "CREDITOS", nullable = false)
    private var creditos: Int = 0

    @Column(name = "HORAS_SEMANALES", nullable = false)
    private var horasSemanales: Int = 0

    constructor() {}

    constructor(codigoCurso: String, nombre: String, creditos: Int, horasSemanales: Int) {
        this.codigoCurso = codigoCurso
        this.nombre = nombre
        this.creditos = creditos
        this.horasSemanales = horasSemanales
    }

    // Getters y Setters
    fun getCodigoCurso(): String {
        return codigoCurso
    }

    fun setCodigoCurso(codigoCurso: String) {
        this.codigoCurso = codigoCurso
    }

    fun getNombre(): String {
        return nombre
    }

    fun setNombre(nombre: String) {
        this.nombre = nombre
    }

    fun getCreditos(): Int {
        return creditos
    }

    fun setCreditos(creditos: Int) {
        this.creditos = creditos
    }

    fun getHorasSemanales(): Int {
        return horasSemanales
    }

    fun setHorasSemanales(horasSemanales: Int) {
        this.horasSemanales = horasSemanales
    }

    // toString
    override fun toString(): String {
        return "Curso(codigoCurso='$codigoCurso', nombre='$nombre', creditos=$creditos, horasSemanales=$horasSemanales)"
    }
}