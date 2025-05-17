/*
    * @author: Derek Rojas Mendoza
    * @author: Joseph León Cabezas
*/

package com.example.lab4.dao

import com.example.lab4.exceptions.GlobalException
import com.example.lab4.exceptions.NoDataException
import com.example.lab4.model.*
import org.hibernate.dialect.OracleTypes
import org.springframework.stereotype.Repository
import java.sql.*
import kotlin.jvm.Throws

@Repository
class DaoMatricula : Dao(), I_DaoMatricula {
    
    private fun tryConnection() {
        try {
            conectar()
        } catch (e: ClassNotFoundException) {
            throw GlobalException("No se ha localizado el driver")
        } catch (e: SQLException) {
            throw NoDataException("La base de datos no se encuentra disponible")
        }
    }

    // ----------------- Matrícula -----------------

    @Throws(GlobalException::class, NoDataException::class)
    override fun registrarMatricula(grupoId: Int, cedulaAlumno: String) {
        tryConnection()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{call MOVILES.REGISTRAR_MATRICULA(?, ?)}")
            pstmt?.setInt(1, grupoId)
            pstmt?.setString(2, cedulaAlumno)

            val resultado = pstmt?.executeUpdate()
            if (resultado == 0) {
                throw NoDataException("No se realizó el registro de la matrícula")
            }
        } catch (e: SQLException) {
            when (e.errorCode) {
                1 -> throw GlobalException("El alumno ya está matriculado en este grupo")
                2291 -> throw GlobalException("Violación de integridad (grupo o alumno no existe)")
                else -> throw GlobalException("Error al registrar matrícula: ${e.message}")
            }
        } finally {
            try {
                pstmt?.close()
                desconectar()
            } catch (e: SQLException) {
                throw GlobalException("Error al cerrar recursos")
            }
        }
    }

    @Throws(GlobalException::class, NoDataException::class)
    override fun eliminarMatricula(grupoId: Int, cedulaAlumno: String) {
        tryConnection()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{call MOVILES.ELIMINAR_MATRICULA(?, ?)}")
            pstmt?.setInt(1, grupoId)
            pstmt?.setString(2, cedulaAlumno)

            val resultado = pstmt?.executeUpdate()
            if (resultado == 0) {
                throw NoDataException("No se encontró la matrícula para eliminar")
            }
        } catch (e: SQLException) {
            throw GlobalException("Error al eliminar matrícula: ${e.message}")
        } finally {
            try {
                pstmt?.close()
                desconectar()
            } catch (e: SQLException) {
                throw GlobalException("Error al cerrar recursos")
            }
        }
    }

    // ----------------- Registro de Notas -----------------

    @Throws(GlobalException::class, NoDataException::class)
    override fun registrarNota(grupoId: Int, cedulaAlumno: String, nota: Int) {
        tryConnection()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{call MOVILES.REGISTRAR_NOTA(?, ?, ?)}")
            pstmt?.setInt(1, grupoId)
            pstmt?.setString(2, cedulaAlumno)
            pstmt?.setInt(3, nota)

            val resultado = pstmt?.executeUpdate()
            if (resultado == 0) {
                throw NoDataException("No se pudo registrar la nota")
            }
        } catch (e: SQLException) {
            when (e.errorCode) {
                1 -> throw GlobalException("Ya existe un registro de nota para este alumno en el grupo")
                2291 -> throw GlobalException("Violación de integridad (grupo o alumno no existe)")
                else -> throw GlobalException("Error al registrar nota: ${e.message}")
            }
        } finally {
            try {
                pstmt?.close()
                desconectar()
            } catch (e: SQLException) {
                throw GlobalException("Error al cerrar recursos")
            }
        }
    }

    // ----------------- Historial Académico -----------------

    @Throws(GlobalException::class, NoDataException::class)
    override fun consultarHistorialAcademico(cedulaAlumno: String): Collection<Matricula> {
        tryConnection()

        var rs: ResultSet? = null
        val coleccion = mutableListOf<Matricula>()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{? = call MOVILES.CONSULTAR_HISTORIAL(?)}")
            pstmt?.registerOutParameter(1, OracleTypes.CURSOR)
            pstmt?.setString(2, cedulaAlumno)
            pstmt?.execute()

            rs = pstmt?.getObject(1) as ResultSet?

            while (rs?.next() == true) {
                coleccion.add(
                    Matricula(
                        matriculaId = rs.getInt("MATRICULA_ID"),
                        grupoId = rs.getInt("GRUPO_ID"),
                        cedulaAlumno = rs.getString("CEDULA_ALUMNO"),
                        nota = rs.getBigDecimal("NOTA")
                    )
                )
            }
        } catch (e: SQLException) {
            throw GlobalException("Error al consultar historial académico: ${e.message}")
        } finally {
            try {
                rs?.close()
                pstmt?.close()
                desconectar()
            } catch (e: SQLException) {
                throw GlobalException("Error al cerrar recursos")
            }
        }

        if (coleccion.isNotEmpty()) {
            return coleccion
        } else {
            throw NoDataException("No se encontraron registros académicos para el alumno")
        }
    }
}