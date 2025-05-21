/*
    * @author: Derek Rojas Mendoza
    * @author: Joseph León Cabezas
*/

package com.example.backend.dao

import com.example.backend.model.*
import org.springframework.stereotype.Repository

@Repository
interface I_DaoMatricula {

    // ----------------- Matrícula -----------------

    fun registrarMatricula(grupoId: Int, cedulaAlumno: String)

    fun eliminarMatricula(grupoId: Int, cedulaAlumno: String)

    // ----------------- Registro de Notas -----------------

    fun registrarNota(grupoId: Int, cedulaAlumno: String, nota: Int)

    // ----------------- Historial Académico -----------------

    fun consultarHistorialAcademico(cedulaAlumno: String): Collection<Matricula>
}