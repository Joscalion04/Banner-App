package com.example.backend.dao

import com.example.backend.exceptions.GlobalException
import com.example.backend.exceptions.NoDataException
import com.example.backend.model.*
import org.hibernate.dialect.OracleTypes
import org.springframework.stereotype.Repository
import java.sql.*
import kotlin.jvm.Throws

@Repository
class DaoProfesores : Dao() {

    private fun tryConnection() {
        try {
            conectar()
        } catch (e: ClassNotFoundException) {
            throw GlobalException("No se ha localizado el driver")
        } catch (e: SQLException) {
            throw NoDataException("La base de datos no se encuentra disponible")
        }
    }


    // ----------------- CRUD para Profesor -----------------

    @Throws(GlobalException::class, NoDataException::class)
    fun insertarProfesor(profesor: Profesor) {
        tryConnection()
        try {
            conexion?.prepareCall("{call MOVILES.INSERTAR_PROFESOR (?, ?, ?, ?)}").use { pstmt ->
                pstmt?.setString(1, profesor.getCedula())
                pstmt?.setString(2, profesor.getNombre())
                pstmt?.setString(3, profesor.getTelefono())
                pstmt?.setString(4, profesor.getEmail())
                pstmt?.execute()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw GlobalException("Error al insertar profesor: ${e.message}")
        } finally {
            desconectar()
        }
    }


    @Throws(GlobalException::class, NoDataException::class)
    fun obtenerProfesores(): Collection<Profesor> {
        this.tryConnection()

        var rs: ResultSet? = null
        val coleccion = mutableListOf<Profesor>()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{? = call MOVILES.OBTENER_PROFESORES()}")
            pstmt?.registerOutParameter(1, OracleTypes.CURSOR) // ✅ OracleTypes.CURSOR obligatorio para funciones que retornan SYS_REFCURSOR
            pstmt?.execute()
            rs = pstmt?.getObject(1) as ResultSet? // ✅ Obtener el cursor como ResultSet

            while (rs?.next() == true) {
                coleccion.add(
                    Profesor(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email")
                    )
                )
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw GlobalException("Error ejecutando obtenerProfesores: ${e.message}")
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
    fun actualizarProfesor(profesor: Profesor) {
        tryConnection()
        try {
            conexion?.prepareCall("{call MOVILES.ACTUALIZAR_PROFESOR (?, ?, ?, ?)}").use { pstmt ->
                pstmt?.setString(1, profesor.getCedula())
                pstmt?.setString(2, profesor.getNombre())
                pstmt?.setString(3, profesor.getTelefono())
                pstmt?.setString(4, profesor.getEmail())

                val resultado = pstmt?.executeUpdate()
                if (resultado == 0) {
                    throw NoDataException("No se realizó la actualización: profesor no existente")
                }
            }
        } catch (e: SQLException) {
            throw GlobalException("Error al actualizar profesor: ${e.message}")
        } finally {
            desconectar()
        }
    }

    @Throws(GlobalException::class, NoDataException::class)
    fun eliminarProfesor(cedula: String) {
        tryConnection()
        try {
            conexion?.prepareCall("{call MOVILES.ELIMINAR_PROFESOR(?)}").use { pstmt ->
                pstmt?.setString(1, cedula)
                val resultado = pstmt?.executeUpdate()
                if (resultado == 0) {
                    throw NoDataException("No se realizó el borrado: profesor no existente")
                }
            }
        } catch (e: SQLException) {
            if (e.errorCode == 2292) {
                throw GlobalException("No se puede eliminar el profesor, existen registros dependientes (violación de integridad)")
            }
            throw GlobalException("Error al eliminar profesor: ${e.message}")
        } finally {
            desconectar()
        }
    }

    @Throws(GlobalException::class, NoDataException::class)
    fun obtenerProfesorPorCedula(cedula: String): Profesor {
        tryConnection()

        var rs: ResultSet? = null
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{ ? = call MOVILES.OBTENER_PROFESOR_POR_CEDULA(?) }")
                ?: throw GlobalException("Conexión no disponible")

            pstmt.registerOutParameter(1, -10) // REF_CURSOR estándar Oracle
            pstmt.setString(2, cedula)

            pstmt.execute()

            rs = pstmt.getObject(1) as? ResultSet ?: throw NoDataException("No se encontró profesor con cédula $cedula")

            if (rs.next()) {
                return Profesor(
                    rs.getString("cedula"),
                    rs.getString("nombre"),
                    rs.getString("telefono"),
                    rs.getString("email")
                )
            } else {
                throw NoDataException("No se encontró profesor con cédula $cedula")
            }

        } catch (e: SQLException) {
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
    }


    @Throws(GlobalException::class, NoDataException::class)
    fun obtenerProfesorPorNombre(nombre: String): Collection<Profesor> {
        tryConnection()

        var rs: ResultSet? = null
        val coleccion = mutableListOf<Profesor>()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{ ? = call MOVILES.OBTENER_PROFESOR_POR_NOMBRE(?) }")
                ?: throw GlobalException("Conexión no disponible")

            pstmt.registerOutParameter(1, -10) // Uso seguro de REF_CURSOR
            pstmt.setString(2, nombre)

            pstmt.execute()

            rs = pstmt.getObject(1) as? ResultSet ?: throw NoDataException("No se encontraron profesores con el nombre $nombre")

            while (rs.next()) {
                coleccion.add(
                    Profesor(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email")
                    )
                )
            }

        } catch (e: SQLException) {
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

        if (coleccion.isNotEmpty()) {
            return coleccion
        } else {
            throw NoDataException("No se encontraron profesores con el nombre $nombre")
        }
    }

}