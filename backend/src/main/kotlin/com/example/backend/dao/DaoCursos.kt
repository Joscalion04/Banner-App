package com.example.backend.dao

import com.example.backend.exceptions.GlobalException
import com.example.backend.exceptions.NoDataException
import com.example.backend.model.*
import org.hibernate.dialect.OracleTypes
import org.springframework.stereotype.Repository
import java.sql.*
import kotlin.jvm.Throws

@Repository
class DaoCursos : Dao(){
    private fun tryConnection() {
        try {
            conectar()
        } catch (e: ClassNotFoundException) {
            throw GlobalException("No se ha localizado el driver")
        } catch (e: SQLException) {
            throw NoDataException("La base de datos no se encuentra disponible")
        }
    }

    // ----------------- CRUD para Curso -----------------

    @Throws(GlobalException::class, NoDataException::class)
    fun insertarCurso(curso: Curso) {
        tryConnection()

        try {
            conexion?.prepareCall("{call MOVILES.INSERTAR_CURSO (?, ?, ?, ?)}").use { pstmt ->
                pstmt?.setString(1, curso.getCodigoCurso())
                pstmt?.setString(2, curso.getNombre())
                pstmt?.setInt(3, curso.getCreditos())
                pstmt?.setInt(4, curso.getHorasSemanales())
                pstmt?.execute() // No validas retorno si no tienes OUT
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw GlobalException("Error al insertar curso: ${e.message}")
        } finally {
            desconectar()
        }
    }


    @Throws(GlobalException::class, NoDataException::class)
    fun obtenerCursos(): Collection<Curso> {
        tryConnection()

        var rs: ResultSet? = null
        val coleccion = mutableListOf<Curso>()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{? = call MOVILES.OBTENER_CURSOS()}")
            pstmt?.registerOutParameter(1, OracleTypes.CURSOR) // ✅ Usar OracleTypes.CURSOR directamente
            pstmt?.execute()
            rs = pstmt?.getObject(1) as ResultSet? // ✅ Cast directo a ResultSet

            while (rs?.next() == true) {
                coleccion.add(
                    Curso(
                        rs.getString("codigo_curso"),
                        rs.getString("nombre"),
                        rs.getInt("creditos"),
                        rs.getInt("horas_semanales")
                    )
                )
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw GlobalException("Error ejecutando obtenerCursos: ${e.message}")
        } finally {
            try {
                rs?.close()
                pstmt?.close()
                desconectar()
            } catch (e: SQLException) {
                throw GlobalException("Error cerrando recursos: ${e.message}")
            }
        }

        if (coleccion.isNotEmpty()) {
            return coleccion
        } else {
            throw NoDataException("No hay datos")
        }
    }

    @Throws(GlobalException::class, NoDataException::class)
    fun actualizarCurso(curso: Curso) {
        tryConnection()

        try {
            val sql = "{call MOVILES.ACTUALIZAR_CURSO(?, ?, ?, ?)}"
            conexion?.prepareCall(sql).use { pstmt ->
                pstmt?.setString(1, curso.getCodigoCurso())
                pstmt?.setString(2, curso.getNombre())
                pstmt?.setInt(3, curso.getCreditos())
                pstmt?.setInt(4, curso.getHorasSemanales())

                val resultado = pstmt?.executeUpdate() ?: 0
                if (resultado == 0) {
                    throw NoDataException("No se realizó la actualización: código no existente.")
                }

                println("\n✅ Modificación satisfactoria del curso!")
            }
        } catch (e: SQLException) {
            println("❌ Error SQL: ${e.message}")
            throw GlobalException("Error al ejecutar la sentencia: ${e.message}")
        } finally {
            desconectar()
        }
    }


    @Throws(GlobalException::class, NoDataException::class)
    fun eliminarCurso(codigo: String) {
        tryConnection()

        try {
            val pstmt = conexion?.prepareCall("{call MOVILES.ELIMINAR_CURSO(?)}")
                ?: throw GlobalException("Conexión no disponible")

            pstmt.use {
                it.setString(1, codigo)
                val resultado = it.executeUpdate()
                if (resultado == 0) {
                    throw NoDataException("No se realizó el borrado, curso no existe o sin cambios")
                }

                println("\n✅ Eliminación satisfactoria del curso!")
            }
        } catch (e: SQLException) {
            if (e.errorCode == 2292) { // ORA-02292: violación de FK
                throw GlobalException("No se puede eliminar el curso, existen registros dependientes (violación de integridad)")
            }
            println("❌ Error SQL: ${e.message}")
            throw GlobalException("Error en la sentencia SQL: ${e.message}")
        } finally {
            try {
                desconectar()
            } catch (e: SQLException) {
                throw GlobalException("Error al cerrar conexión")
            }
        }
    }


    @Throws(GlobalException::class, NoDataException::class)
    fun obtenerCursoPorCodigo(codigo: String): Curso {
        tryConnection()
        var rs: ResultSet? = null
        var elCurso: Curso? = null
        var pstmt: CallableStatement? = null
        try {
            pstmt = conexion?.prepareCall("{ ? = call MOVILES.OBTENER_CURSO_POR_CODIGO(?) }")
                ?: throw GlobalException("Conexión no disponible")
            pstmt.registerOutParameter(1, -10) // REF_CURSOR para Oracle
            pstmt.setString(2, codigo)
            pstmt.execute()
            rs = pstmt.getObject(1) as? ResultSet ?: throw NoDataException("No se encontró el curso")
            if (rs.next()) {
                elCurso = Curso(
                    rs.getString("codigo_curso"),
                    rs.getString("nombre"),
                    rs.getInt("creditos"),
                    rs.getInt("horas_semanales")
                )
            } else {
                throw NoDataException("No se encontró el curso")
            }
        } catch (e: SQLException) {
            println("❌ Error SQL: ${e.message}")
            throw GlobalException("Error en la consulta: ${e.message}")
        } finally {
            try {
                rs?.close()
                pstmt?.close()
                desconectar()
            } catch (e: SQLException) {
                throw GlobalException("Error al cerrar recursos")
            }
        }
        return elCurso ?: throw NoDataException("No se encontró el curso")
    }

    @Throws(GlobalException::class, NoDataException::class)
    fun obtenerCursosPorNombre(nombre: String): Collection<Curso> {
        tryConnection()
        var rs: ResultSet? = null
        val cursos = mutableListOf<Curso>()
        var pstmt: CallableStatement? = null
        try {
            pstmt = conexion?.prepareCall("{ ? = call MOVILES.OBTENER_CURSOS_POR_NOMBRE(?) }")
                ?: throw GlobalException("Conexión no disponible")
            pstmt.registerOutParameter(1, -10) // REF_CURSOR para Oracle
            pstmt.setString(2, nombre)
            pstmt.execute()
            rs = pstmt.getObject(1) as? ResultSet ?: throw NoDataException("No hay cursos para el nombre ingresado")
            while (rs.next()) {
                cursos.add(
                    Curso(
                        rs.getString("codigo_curso"),
                        rs.getString("nombre"),
                        rs.getInt("creditos"),
                        rs.getInt("horas_semanales")
                    )
                )
            }
        } catch (e: SQLException) {
            println("❌ Error SQL: ${e.message}")
            throw GlobalException("Error en la consulta: ${e.message}")
        } finally {
            try {
                rs?.close()
                pstmt?.close()
                desconectar()
            } catch (e: SQLException) {
                throw GlobalException("Error al cerrar recursos")
            }
        }
        if (cursos.isEmpty()) {
            throw NoDataException("No se encontraron cursos con el nombre proporcionado")
        }
        return cursos
    }


    @Throws(GlobalException::class, NoDataException::class)
    fun obtenerCursosPorCarrera(codigoCarrera: String): Collection<Curso> {
        tryConnection()
        var rs: ResultSet? = null
        val cursos = mutableListOf<Curso>()
        var pstmt: CallableStatement? = null
        try {
            pstmt = conexion?.prepareCall("{ ? = call MOVILES.OBTENER_CURSOS_POR_CARRERA(?) }")
                ?: throw GlobalException("Conexión no disponible")
            pstmt.registerOutParameter(1, -10) // REF_CURSOR Oracle
            pstmt.setString(2, codigoCarrera)
            pstmt.execute()
            rs = pstmt.getObject(1) as? ResultSet ?: throw NoDataException("No se encontraron cursos para la carrera proporcionada")
            while (rs.next()) {
                cursos.add(
                    Curso(
                        rs.getString("codigo_curso"),
                        rs.getString("nombre"),
                        rs.getInt("creditos"),
                        rs.getInt("horas_semanales")
                    )
                )
            }
        } catch (e: SQLException) {
            println("❌ Error SQL: ${e.message}")
            throw GlobalException("Error en la consulta: ${e.message}")
        } finally {
            try {
                rs?.close()
                pstmt?.close()
                desconectar()
            } catch (e: SQLException) {
                throw GlobalException("Error al cerrar recursos")
            }
        }
        if (cursos.isEmpty()) {
            throw NoDataException("No se encontraron cursos asociados a la carrera")
        }
        return cursos
    }


}