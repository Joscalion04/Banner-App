/*
    * @author: Derek Rojas Mendoza
    * @author: Joseph León Cabezas
*/

package com.example.backend.model

import jakarta.persistence.*

@Entity
@Table(name = "USUARIO", schema = "MOVILES")
class Usuario {
    @Id
    @Column(name = "CEDULA", length = 20, nullable = false)
    private var cedula: String = ""

    @Column(name = "CLAVE", length = 20, nullable = false)
    private var clave: String = ""

    @Column(name = "TIPO_USUARIO", length = 20, nullable = false)
    private var tipoUsuario: String = ""

    companion object {
        const val TIPO_ADMINISTRADOR = "ADMINISTRADOR"
        const val TIPO_MATRICULADOR = "MATRICULADOR"
        const val TIPO_PROFESOR = "PROFESOR"
        const val TIPO_ALUMNO = "ALUMNO"
    }

    constructor() {}

    constructor(cedula: String, clave: String, tipoUsuario: String) {
        this.cedula = cedula
        this.clave = clave
        setTipoUsuario(tipoUsuario) // Usa el setter para validar
    }

    // Getters y Setters
    fun getCedula(): String {
        return cedula
    }

    fun setCedula(cedula: String) {
        this.cedula = cedula
    }

    fun getClave(): String {
        return clave
    }

    fun setClave(clave: String) {
        this.clave = clave
    }

    fun getTipoUsuario(): String {
        return tipoUsuario
    }

    fun setTipoUsuario(tipoUsuario: String) {
        if (tipoUsuario == TIPO_ADMINISTRADOR ||
            tipoUsuario == TIPO_MATRICULADOR ||
            tipoUsuario == TIPO_PROFESOR ||
            tipoUsuario == TIPO_ALUMNO) {
            this.tipoUsuario = tipoUsuario
        } else {
            throw IllegalArgumentException("Tipo de usuario no válido")
        }
    }

    // toString
    override fun toString(): String {
        return "Usuario(cedula='$cedula', tipoUsuario='$tipoUsuario')"
    }
}