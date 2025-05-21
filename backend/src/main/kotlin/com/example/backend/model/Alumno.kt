/*
    * @author: Derek Rojas Mendoza
    * @author: Joseph León Cabezas
*/

package com.example.backend.model

import jakarta.persistence.*
import java.util.Date

@Entity
@Table(name = "ALUMNO", schema = "MOVILES")
class Alumno {
    @Id
    @Column(name = "CEDULA", length = 20, nullable = false)
    private var cedula: String = ""

    @Column(name = "NOMBRE", length = 100, nullable = false)
    private var nombre: String = ""

    @Column(name = "TELEFONO", length = 20)
    private var telefono: String? = null

    @Column(name = "EMAIL", length = 100, nullable = false)
    private var email: String = ""

    @Column(name = "FECHA_NACIMIENTO", nullable = false)
    private var fechaNacimiento: Date = Date()

    @Column(name = "CODIGO_CARRERA", length = 20, nullable = false)
    private var codigoCarrera: String = ""

    constructor() {}

    constructor(cedula: String, nombre: String, telefono: String?, email: String, fechaNacimiento: Date, codigoCarrera: String) {
        this.cedula = cedula
        this.nombre = nombre
        this.telefono = telefono
        this.email = email
        this.fechaNacimiento = fechaNacimiento
        this.codigoCarrera = codigoCarrera
    }

    // Getters y Setters
    fun getCedula(): String {
        return cedula
    }

    fun setCedula(cedula: String) {
        this.cedula = cedula
    }

    fun getNombre(): String {
        return nombre
    }

    fun setNombre(nombre: String) {
        this.nombre = nombre
    }

    fun getTelefono(): String? {
        return telefono
    }

    fun setTelefono(telefono: String?) {
        this.telefono = telefono
    }

    fun getEmail(): String {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getFechaNacimiento(): Date {
        return fechaNacimiento
    }

    fun setFechaNacimiento(fechaNacimiento: Date) {
        this.fechaNacimiento = fechaNacimiento
    }

    fun getCodigoCarrera(): String {
        return codigoCarrera
    }

    fun setCodigoCarrera(codigoCarrera: String) {
        this.codigoCarrera = codigoCarrera
    }

    // toString
    override fun toString(): String {
        return "Alumno(cedula='$cedula', nombre='$nombre', telefono=$telefono, email='$email', fechaNacimiento=$fechaNacimiento, codigoCarrera='$codigoCarrera')"
    }
}