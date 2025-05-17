package com.example.lab4.dao

import com.example.lab4.exceptions.GlobalException
import com.example.lab4.exceptions.NoDataException
import com.example.lab4.model.*
import org.hibernate.dialect.OracleTypes
import org.springframework.stereotype.Repository
import java.sql.*
import kotlin.jvm.Throws

@Repository
class DaoCarreras : Dao(){

    private fun tryConnection() {
        try {
            conectar()
        } catch (e: ClassNotFoundException) {
            throw GlobalException("No se ha localizado el driver")
        } catch (e: SQLException) {
            throw NoDataException("La base de datos no se encuentra disponible")
        }
    }


    // ----------------- CRUD para Carrera -----------------

    @Throws(GlobalException::class, NoDataException::class)
    fun insertarCarrera(carrera: Carrera) {
        tryConnection()

        try {
            conexion?.prepareCall("{call MOVILES.INSERTAR_CARRERA(?, ?, ?)}").use { pstmt ->
                pstmt?.setString(1, carrera.getCodigoCarrera())
                pstmt?.setString(2, carrera.getNombre())
                pstmt?.setString(3, carrera.getTitulo())
                pstmt?.execute() // No necesitas validar retorno si no hay OUT
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw GlobalException("Error al insertar carrera: ${e.message}")
        } finally {
            desconectar()
        }
    }

    @Throws(GlobalException::class, NoDataException::class)
     fun obtenerCarreras(): Collection<Carrera> {
        this.tryConnection()

        var rs: ResultSet? = null
        val coleccion = mutableListOf<Carrera>()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{? = call MOVILES.OBTENER_CARRERAS()}")
            pstmt?.registerOutParameter(1, OracleTypes.CURSOR) // ✅ Aquí es clave OracleTypes.CURSOR
            pstmt?.execute()
            rs = pstmt?.getObject(1) as ResultSet? // ✅ Se obtiene el cursor

            while (rs?.next() == true) {
                coleccion.add(
                    Carrera(
                        rs.getString("codigo_carrera"),
                        rs.getString("nombre"),
                        rs.getString("titulo")
                    )
                )
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw GlobalException("Error ejecutando obtenerCarreras: ${e.message}")
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
    fun actualizarCarrera(carrera: Carrera) {
        tryConnection()  // Asegura que 'conexion' no sea null y esté abierta

        try {
            val sql = "{call MOVILES.ACTUALIZAR_CARRERA(?, ?, ?)}"
            conexion?.prepareCall(sql)?.use { pstmt ->
                pstmt.setString(1, carrera.getCodigoCarrera())
                pstmt.setString(2, carrera.getNombre())
                pstmt.setString(3, carrera.getTitulo())

                val resultado = pstmt.executeUpdate()
                if (resultado == 0) {
                    throw NoDataException("No se realizó la actualización: código no existente.")
                }

                println("\n✅ Modificación satisfactoria!")
            }
        } catch (e: SQLException) {
            println("❌ Error SQL: ${e.message}")
            throw GlobalException("Error al ejecutar la sentencia: ${e.message}")
        } finally {
            desconectar() // Siempre cerrar conexión en finally
        }
    }


    @Throws(GlobalException::class, NoDataException::class)
    fun eliminarCarrera(codigo: String) {
        tryConnection()

        try {
            val pstmt = conexion?.prepareCall("{call MOVILES.ELIMINAR_CARRERA(?)}")
                ?: throw GlobalException("Conexión no disponible")

            pstmt.use {
                it.setString(1, codigo)
                val resultado = it.executeUpdate()
                if (resultado == 0) {
                    throw NoDataException("No se realizó el borrado, carrera no existe o sin cambios")
                }
            }
        } catch (e: SQLException) {
            if (e.errorCode == 2292) { // ORA-02292: violación de FK
                throw GlobalException("No se puede eliminar la carrera, existen registros dependientes (violación de integridad)")
            }
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
    fun obtenerCarreraPorCodigo(codigo: String): Carrera {
        tryConnection()

        var rs: ResultSet? = null
        var laCarrera: Carrera? = null
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{ ? = call MOVILES.OBTENER_CARRERA_POR_CODIGO(?) }")
                ?: throw GlobalException("Conexión no disponible")

            pstmt.registerOutParameter(1, -10) // Uso del valor literal -10 que representa REF_CURSOR
            pstmt.setString(2, codigo)

            pstmt.execute()

            rs = pstmt.getObject(1) as? ResultSet ?: throw NoDataException("No se encontró la carrera")

            if (rs.next()) {
                laCarrera = Carrera(
                    rs.getString("codigo_carrera"),
                    rs.getString("nombre"),
                    rs.getString("titulo")
                )
            } else {
                throw NoDataException("No se encontró la carrera")
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

        return laCarrera ?: throw NoDataException("No se encontró la carrera")
    }

    @Throws(GlobalException::class, NoDataException::class)
    fun obtenerCarreraPorNombre(nombre: String): Collection<Carrera> {
        tryConnection()

        var rs: ResultSet? = null
        val coleccion = mutableListOf<Carrera>()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{ ? = call MOVILES.OBTENER_CARRERA_POR_NOMBRE(?) }")
                ?: throw GlobalException("Conexión no disponible")

            pstmt.registerOutParameter(1, -10) // Uso del valor literal -10 para REF_CURSOR
            pstmt.setString(2, nombre)

            pstmt.execute()

            rs = pstmt.getObject(1) as? ResultSet ?: throw NoDataException("No se encontraron carreras")

            while (rs.next()) {
                coleccion.add(
                    Carrera(
                        rs.getString("codigo_carrera"),
                        rs.getString("nombre"),
                        rs.getString("titulo")
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
            throw NoDataException("No se encontraron carreras")
        }
    }

}