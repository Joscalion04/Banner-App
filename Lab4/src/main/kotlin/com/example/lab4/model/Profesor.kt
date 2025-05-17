/*
    * @author: Derek Rojas Mendoza
    * @author: Joseph León Cabezas
*/


package com.example.lab4.model

import jakarta.persistence.*

@Entity
@Table(name = "PROFESOR", schema = "MOVILES")
class Profesor {
    @Id
    @Column(name = "CEDULA", length = 20, nullable = false)
    private var cedula: String = ""

    @Column(name = "NOMBRE", length = 100, nullable = false)
    private var nombre: String = ""

    @Column(name = "TELEFONO", length = 20)
    private var telefono: String? = null

    @Column(name = "EMAIL", length = 100, nullable = false)
    private var email: String = ""

    constructor() {}

    constructor(cedula: String, nombre: String, telefono: String?, email: String) {
        this.cedula = cedula
        this.nombre = nombre
        this.telefono = telefono
        this.email = email
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

    // toString
    override fun toString(): String {
        return "Profesor(cedula='$cedula', nombre='$nombre', telefono=$telefono, email='$email')"
    }
}