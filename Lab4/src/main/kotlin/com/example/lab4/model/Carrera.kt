/*
    * @author: Derek Rojas Mendoza
    * @author: Joseph León Cabezas
*/

package com.example.lab4.model

import jakarta.persistence.*

@Entity
@Table(name = "CARRERA", schema = "MOVILES")
class Carrera {
    @Id
    @Column(name = "CODIGO_CARRERA", length = 20, nullable = false)
    private var codigoCarrera: String = ""

    @Column(name = "NOMBRE", length = 100, nullable = false)
    private var nombre: String = ""

    @Column(name = "TITULO", length = 100, nullable = false)
    private var titulo: String = ""

    constructor() {}

    constructor(codigoCarrera: String, nombre: String, titulo: String) {
        this.codigoCarrera = codigoCarrera
        this.nombre = nombre
        this.titulo = titulo
    }

    // Getters y Setters
    fun getCodigoCarrera(): String {
        return codigoCarrera
    }

    fun setCodigoCarrera(codigoCarrera: String) {
        this.codigoCarrera = codigoCarrera
    }

    fun getNombre(): String {
        return nombre
    }

    fun setNombre(nombre: String) {
        this.nombre = nombre
    }

    fun getTitulo(): String {
        return titulo
    }

    fun setTitulo(titulo: String) {
        this.titulo = titulo
    }

    // toString
    override fun toString(): String {
        return "Carrera(codigoCarrera='$codigoCarrera', nombre='$nombre', titulo='$titulo')"
    }
}