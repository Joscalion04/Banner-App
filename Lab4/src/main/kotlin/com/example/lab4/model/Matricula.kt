/*
    * @author: Derek Rojas Mendoza
    * @author: Joseph León Cabezas
*/


package com.example.lab4.model

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "MATRICULA", schema = "MOVILES")
class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MATRICULA_ID")
    var matriculaId: Int = 0

    @Column(name = "GRUPO_ID", nullable = false)
    var grupoId: Int = 0

    @Column(name = "CEDULA_ALUMNO", length = 20, nullable = false)
    var cedulaAlumno: String = ""

    @Column(name = "NOTA")
    var nota: BigDecimal? = null

    constructor()

    constructor(matriculaId: Int, grupoId: Int, cedulaAlumno: String, nota: BigDecimal? = null) {
        this.matriculaId = matriculaId
        this.grupoId = grupoId
        this.cedulaAlumno = cedulaAlumno
        this.nota = nota
    }

    override fun toString(): String {
        return "Matricula(matriculaId=$matriculaId, grupoId=$grupoId, cedulaAlumno='$cedulaAlumno', nota=$nota)"
    }
}
