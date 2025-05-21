package com.example.backend.dao

import com.example.backend.exceptions.GlobalException
import com.example.backend.exceptions.NoDataException
import com.example.backend.model.*
import org.hibernate.dialect.OracleTypes
import org.springframework.stereotype.Repository
import java.sql.*
import kotlin.jvm.Throws

@Repository
class DaoGrupos : Dao(){

    private fun tryConnection() {
        try {
            conectar()
        } catch (e: ClassNotFoundException) {
            throw GlobalException("No se ha localizado el driver")
        } catch (e: SQLException) {
            throw NoDataException("La base de datos no se encuentra disponible")
        }
    }

    // ----------------- CRUD para Grupo -----------------

    @Throws(GlobalException::class, NoDataException::class)
    fun insertarGrupo(grupo: Grupo) {
        tryConnection()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{call MOVILES.INSERTAR_GRUPO(?, ?, ?, ?, ?)}")
            pstmt?.setInt(1, grupo.getCicloId())
            pstmt?.setString(2, grupo.getCodigoCurso())
            pstmt?.setInt(3, grupo.getNumeroGrupo())
            pstmt?.setString(4, grupo.getHorario())
            pstmt?.setString(5, grupo.getCedulaProfesor())

            val resultado = pstmt?.executeUpdate()
            if (resultado == 0) {
                throw NoDataException("No se realizó la inserción del grupo")
            }
        } catch (e: SQLException) {
            when (e.errorCode) {
                1 -> throw GlobalException("Ya existe un grupo con estos parámetros")
                2291 -> throw GlobalException("Violación de integridad referencial (ciclo, curso o profesor no existe)")
                else -> throw GlobalException("Error al insertar grupo: ${e.message}")
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
    fun obtenerGrupos(): Collection<Grupo> {
        tryConnection()

        var rs: ResultSet? = null
        val coleccion = mutableListOf<Grupo>()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{? = call MOVILES.OBTENER_GRUPOS()}")
            pstmt?.registerOutParameter(1, OracleTypes.CURSOR)
            pstmt?.execute()
            rs = pstmt?.getObject(1) as ResultSet?

            while (rs?.next() == true) {
                coleccion.add(
                    Grupo(
                        rs.getInt("grupo_id"),
                        rs.getInt("ciclo_id"),
                        rs.getString("codigo_curso"),
                        rs.getInt("numero_grupo"),
                        rs.getString("horario"),
                        rs.getString("cedula_profesor")
                    )
                )
            }
        } catch (e: SQLException) {
            throw GlobalException("Error al obtener grupos: ${e.message}")
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
            throw NoDataException("No se encontraron grupos registrados")
        }
    }


    @Throws(GlobalException::class, NoDataException::class)
    fun actualizarGrupo(grupo: Grupo) {
        tryConnection()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{call MOVILES.ACTUALIZAR_GRUPO(?, ?, ?)}")
            pstmt?.setInt(1, grupo.getGrupoId())
            pstmt?.setString(2, grupo.getHorario())
            pstmt?.setString(3, grupo.getCedulaProfesor())

            val resultado = pstmt?.executeUpdate()
            if (resultado == 0) {
                throw NoDataException("No se realizó la actualización: grupo no encontrado")
            }
        } catch (e: SQLException) {
            if (e.errorCode == 2291) {
                throw GlobalException("La cédula del profesor no existe")
            }
            throw GlobalException("Error al actualizar grupo: ${e.message}")
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
    fun eliminarGrupo(grupoId: Int) {
        tryConnection()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{call MOVILES.ELIMINAR_GRUPO(?)}")
            pstmt?.setInt(1, grupoId)

            val resultado = pstmt?.executeUpdate()
            if (resultado == 0) {
                throw NoDataException("No se realizó la eliminación: grupo no encontrado")
            }
        } catch (e: SQLException) {
            if (e.errorCode == 2292) {
                throw GlobalException("No se puede eliminar el grupo, tiene estudiantes matriculados")
            }
            throw GlobalException("Error al eliminar grupo: ${e.message}")
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
    fun obtenerGrupoPorId(grupoId: Int): Grupo {
        tryConnection()

        var rs: ResultSet? = null
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{? = call MOVILES.OBTENER_GRUPO_POR_ID(?)}") ?:
                    throw GlobalException("No se pudo preparar la llamada al procedimiento")

            pstmt.registerOutParameter(1, OracleTypes.CURSOR)
            pstmt.setInt(2, grupoId)
            pstmt.execute()

            rs = pstmt.getObject(1) as? ResultSet ?:
                    throw GlobalException("No se pudo obtener el resultado de la consulta")

            if (rs.next()) {
                return Grupo(
                    rs.getInt("grupo_id"),
                    rs.getInt("ciclo_id"),
                    rs.getString("codigo_curso"),
                    rs.getInt("numero_grupo"),
                    rs.getString("horario"),
                    rs.getString("cedula_profesor")
                )
            } else {
                throw NoDataException("No se encontró el grupo con ID $grupoId")
            }
        } catch (e: SQLException) {
            throw GlobalException("Error al obtener grupo por ID: ${e.message}")
        } finally {
            try {
                rs?.close()
                pstmt?.close()
            } catch (e: SQLException) {
                // Logear el error pero no lanzar excepción para no enmascarar errores previos
                println("Error al cerrar recursos: ${e.message}")
            }
            desconectar()
        }
    }
}