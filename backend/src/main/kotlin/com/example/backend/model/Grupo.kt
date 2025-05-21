/*
    * @author: Derek Rojas Mendoza
    * @author: Joseph León Cabezas
*/

package com.example.backend.model

import jakarta.persistence.*

@Entity
@Table(name = "GRUPO", schema = "MOVILES")
class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GRUPO_ID")
    private var grupoId: Int = 0

    @Column(name = "CICLO_ID", nullable = false)
    private var cicloId: Int = 0

    @Column(name = "CODIGO_CURSO", length = 20, nullable = false)
    private var codigoCurso: String = ""

    @Column(name = "NUMERO_GRUPO", nullable = false)
    private var numeroGrupo: Int = 0

    @Column(name = "HORARIO", length = 100, nullable = false)
    private var horario: String = ""

    @Column(name = "CEDULA_PROFESOR", length = 20, nullable = false)
    private var cedulaProfesor: String = ""

    constructor() {}

    constructor(grupoId: Int, cicloId: Int, codigoCurso: String,
                numeroGrupo: Int, horario: String, cedulaProfesor: String) {
        this.grupoId = grupoId
        this.cicloId = cicloId
        this.codigoCurso = codigoCurso
        this.numeroGrupo = numeroGrupo
        this.horario = horario
        this.cedulaProfesor = cedulaProfesor
    }

    // Getters y Setters
    fun getGrupoId(): Int = grupoId
    fun setGrupoId(grupoId: Int) { this.grupoId = grupoId }

    fun getCicloId(): Int = cicloId
    fun setCicloId(cicloId: Int) { this.cicloId = cicloId }

    fun getCodigoCurso(): String = codigoCurso
    fun setCodigoCurso(codigoCurso: String) { this.codigoCurso = codigoCurso }

    fun getNumeroGrupo(): Int = numeroGrupo
    fun setNumeroGrupo(numeroGrupo: Int) { this.numeroGrupo = numeroGrupo }

    fun getHorario(): String = horario
    fun setHorario(horario: String) { this.horario = horario }

    fun getCedulaProfesor(): String = cedulaProfesor
    fun setCedulaProfesor(cedulaProfesor: String) { this.cedulaProfesor = cedulaProfesor }

    // toString actualizado
    override fun toString(): String {
        return "Grupo(grupoId=$grupoId, cicloId=$cicloId, codigoCurso='$codigoCurso', " +
                "numeroGrupo=$numeroGrupo, horario='$horario', cedulaProfesor='$cedulaProfesor')"
    }
}