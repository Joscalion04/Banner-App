package com.example.backend.dao
import com.example.backend.exceptions.GlobalException
import com.example.backend.exceptions.NoDataException
import com.example.backend.model.*
import org.hibernate.dialect.OracleTypes
import org.springframework.stereotype.Repository
import java.sql.*
import kotlin.jvm.Throws

@Repository
class DaoAlumnos : Dao() {

    private fun tryConnection() {
        try {
            conectar()
        } catch (e: ClassNotFoundException) {
            throw GlobalException("No se ha localizado el driver")
        } catch (e: SQLException) {
            throw NoDataException("La base de datos no se encuentra disponible")
        }
    }

    @Throws(GlobalException::class, NoDataException::class)
    fun insertarAlumno(alumno: Alumno) {
        tryConnection()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{call MOVILES.INSERTAR_ALUMNO (?, ?, ?, ?, ?, ?)}")
            pstmt?.setString(1, alumno.getCedula())
            pstmt?.setString(2, alumno.getNombre())
            pstmt?.setString(3, alumno.getTelefono())
            pstmt?.setString(4, alumno.getEmail())

            // Corrección importante para la fecha:
            val fechaNacimiento = java.sql.Date(alumno.getFechaNacimiento().time)
            pstmt?.setDate(5, fechaNacimiento)

            pstmt?.setString(6, alumno.getCodigoCarrera())
            pstmt?.execute()
        } catch (e: SQLException) {
            if (e.errorCode == 1) { // Código de error para violación de constraint única
                throw GlobalException("Ya existe un alumno con esta cédula")
            } else if (e.errorCode == 2291) { // Violación de integridad referencial (carrera no existe)
                throw GlobalException("El código de carrera no existe")
            }
            throw GlobalException("Error al insertar alumno: ${e.message}")
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
    fun obtenerAlumnos(): Collection<Alumno> {
        this.tryConnection()

        var rs: ResultSet? = null
        val coleccion = mutableListOf<Alumno>()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{? = call MOVILES.OBTENER_ALUMNOS()}")
            pstmt?.registerOutParameter(1, OracleTypes.CURSOR) // ✅ OracleTypes.CURSOR porque retorna SYS_REFCURSOR
            pstmt?.execute()
            rs = pstmt?.getObject(1) as ResultSet? // ✅ Obtener el cursor como ResultSet

            while (rs?.next() == true) {
                coleccion.add(
                    Alumno(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getDate("fecha_nacimiento"),
                        rs.getString("codigo_carrera")
                    )
                )
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw GlobalException("Error ejecutando obtenerAlumnos: ${e.message}")
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
    fun actualizarAlumno(alumno: Alumno) {
        tryConnection()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{call MOVILES.ACTUALIZAR_ALUMNO (?, ?, ?, ?, ?, ?)}")
            pstmt?.setString(1, alumno.getCedula())
            pstmt?.setString(2, alumno.getNombre())
            pstmt?.setString(3, alumno.getTelefono())
            pstmt?.setString(4, alumno.getEmail())

            // Corrección para la fecha (igual que en insertar)
            val fechaNacimiento = java.sql.Date(alumno.getFechaNacimiento().time)
            pstmt?.setDate(5, fechaNacimiento)

            pstmt?.setString(6, alumno.getCodigoCarrera())

            val resultado = pstmt?.executeUpdate()
            if (resultado == 0) {
                throw NoDataException("No se realizó la actualización: alumno no existente")
            }
        } catch (e: SQLException) {
            if (e.errorCode == 2291) { // Violación de integridad referencial
                throw GlobalException("El código de carrera no existe")
            }
            throw GlobalException("Error al actualizar alumno: ${e.message}")
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
    fun eliminarAlumno(cedula: String) {
        tryConnection()
        try {
            conexion?.prepareCall("{call MOVILES.ELIMINAR_ALUMNO(?)}").use { pstmt ->
                pstmt?.setString(1, cedula)
                val resultado = pstmt?.executeUpdate()
                if (resultado == 0) {
                    throw NoDataException("No se realizó el borrado: alumno no existente")
                }
            }
        } catch (e: SQLException) {
            if (e.errorCode == 2292) {
                throw GlobalException("No se puede eliminar el alumno, existen registros dependientes (violación de integridad)")
            }
            throw GlobalException("Error al eliminar alumno: ${e.message}")
        } finally {
            desconectar()
        }
    }

    @Throws(GlobalException::class, NoDataException::class)
    fun obtenerAlumnoPorCedula(cedula: String): Alumno {
        tryConnection()

        var rs: ResultSet? = null
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{ ? = call MOVILES.OBTENER_ALUMNO_POR_CEDULA(?) }")
                ?: throw GlobalException("Conexión no disponible")

            pstmt.registerOutParameter(1, OracleTypes.CURSOR)
            pstmt.setString(2, cedula)

            pstmt.execute()

            rs = pstmt.getObject(1) as? ResultSet ?: throw NoDataException("No se encontró alumno con cédula $cedula")

            if (rs.next()) {
                return Alumno(
                    rs.getString("cedula"),
                    rs.getString("nombre"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getDate("fecha_nacimiento"),
                    rs.getString("codigo_carrera")
                )
            } else {
                throw NoDataException("No se encontró alumno con cédula $cedula")
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
    fun obtenerAlumnoPorNombre(nombre: String): Collection<Alumno> {
        tryConnection()

        var rs: ResultSet? = null
        val coleccion = mutableListOf<Alumno>()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{ ? = call MOVILES.OBTENER_ALUMNO_POR_NOMBRE(?) }")
                ?: throw GlobalException("Conexión no disponible")

            pstmt.registerOutParameter(1, OracleTypes.CURSOR)
            pstmt.setString(2, nombre)

            pstmt.execute()

            rs = pstmt.getObject(1) as? ResultSet ?: throw NoDataException("No se encontraron alumnos con el nombre $nombre")

            while (rs.next()) {
                coleccion.add(
                    Alumno(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getDate("fecha_nacimiento"),
                        rs.getString("codigo_carrera")
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
            throw NoDataException("No se encontraron alumnos con el nombre $nombre")
        }
    }

    @Throws(GlobalException::class, NoDataException::class)
    fun obtenerAlumnosPorCarrera(codigoCarrera: String): Collection<Alumno> {
        tryConnection()

        var rs: ResultSet? = null
        val coleccion = mutableListOf<Alumno>()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{ ? = call MOVILES.OBTENER_ALUMNOS_POR_CARRERA(?) }")
                ?: throw GlobalException("Conexión no disponible")

            pstmt.registerOutParameter(1, OracleTypes.CURSOR)
            pstmt.setString(2, codigoCarrera)

            pstmt.execute()

            rs = pstmt.getObject(1) as? ResultSet ?: throw NoDataException("No se encontraron alumnos para la carrera $codigoCarrera")

            while (rs.next()) {
                coleccion.add(
                    Alumno(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getDate("fecha_nacimiento"),
                        rs.getString("codigo_carrera")
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
            throw NoDataException("No se encontraron alumnos para la carrera $codigoCarrera")
        }
    }

}