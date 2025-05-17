package com.example.lab4.dao

import com.example.lab4.exceptions.GlobalException
import com.example.lab4.exceptions.NoDataException
import com.example.lab4.model.*
import org.hibernate.dialect.OracleTypes
import org.springframework.stereotype.Repository
import java.sql.*
import kotlin.jvm.Throws

@Repository
class DaoCiclos : Dao(){

    private fun tryConnection() {
        try {
            conectar()
        } catch (e: ClassNotFoundException) {
            throw GlobalException("No se ha localizado el driver")
        } catch (e: SQLException) {
            throw NoDataException("La base de datos no se encuentra disponible")
        }
    }

    // ----------------- CRUD para Ciclo -----------------

    @Throws(GlobalException::class, NoDataException::class)
    fun insertarCiclo(ciclo: Ciclo) {
        tryConnection()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{call MOVILES.INSERTAR_CICLO (?, ?, ?, ?, ?)}")
            pstmt?.setInt(1, ciclo.getAnio())
            pstmt?.setInt(2, ciclo.getNumero())

            // Conversión correcta de fechas
            pstmt?.setDate(3, java.sql.Date(ciclo.getFechaInicio().time))
            pstmt?.setDate(4, java.sql.Date(ciclo.getFechaFin().time))

            // Corrección clave: Convertir boolean a número (1/0)
            pstmt?.setInt(5, if (ciclo.isActivo()) 1 else 0)

            pstmt?.execute()
        } catch (e: SQLException) {
            when (e.errorCode) {
                1 -> throw GlobalException("Ya existe un ciclo con este año y número")
                2291 -> throw GlobalException("Violación de integridad referencial")
                else -> throw GlobalException("Error al insertar ciclo: ${e.message}")
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
    fun obtenerCiclos(): Collection<Ciclo> {
        this.tryConnection()

        var rs: ResultSet? = null
        val coleccion = mutableListOf<Ciclo>()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{? = call MOVILES.OBTENER_CICLOS()}")
            pstmt?.registerOutParameter(1, OracleTypes.CURSOR) // ✅ OracleTypes.CURSOR por SYS_REFCURSOR
            pstmt?.execute()
            rs = pstmt?.getObject(1) as ResultSet? // ✅ Cast correcto a ResultSet

            while (rs?.next() == true) {
                coleccion.add(
                    Ciclo(
                        rs.getInt("ciclo_id"),
                        rs.getInt("anio"),
                        rs.getInt("numero"),
                        rs.getDate("fecha_inicio"),
                        rs.getDate("fecha_fin"),
                        rs.getBoolean("activo")
                    )
                )
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw GlobalException("Error ejecutando obtenerCiclos: ${e.message}")
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
    fun actualizarCiclo(ciclo: Ciclo) {
        tryConnection()
        var pstmt: CallableStatement? = null

        try {
            // El procedimiento espera 5 parámetros (sin cicloId)
            pstmt = conexion?.prepareCall("{call MOVILES.ACTUALIZAR_CICLO(?, ?, ?, ?, ?)}")

            // Orden exacto de parámetros según el procedure:
            pstmt?.setInt(1, ciclo.getAnio())          // P_ANIO
            pstmt?.setInt(2, ciclo.getNumero())        // P_NUMERO
            pstmt?.setDate(3, java.sql.Date(ciclo.getFechaInicio().time))  // P_FECHA_INICIO
            pstmt?.setDate(4, java.sql.Date(ciclo.getFechaFin().time))     // P_FECHA_FIN
            pstmt?.setInt(5, if (ciclo.isActivo()) 1 else 0)  // P_ACTIVO (convertido a NUMBER)

            val resultado = pstmt?.executeUpdate()
            if (resultado == 0) {
                throw NoDataException("No se realizó la actualización: ciclo no encontrado para el año ${ciclo.getAnio()}")
            }
        } catch (e: SQLException) {
            throw GlobalException("Error al actualizar ciclo: ${e.message}")
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
    fun eliminarCiclo(anio: Int, numero: Int) {
        tryConnection()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{call MOVILES.ELIMINAR_CICLO(?, ?)}")
            pstmt?.setInt(1, anio)    // P_ANIO
            pstmt?.setInt(2, numero)  // P_NUMERO

            val resultado = pstmt?.executeUpdate()
            if (resultado == 0) {
                throw NoDataException("No se encontró ciclo con año $anio y número $numero para eliminar")
            }
        } catch (e: SQLException) {
            if (e.errorCode == 2292) {
                throw GlobalException("No se puede eliminar el ciclo, tiene registros dependientes")
            }
            throw GlobalException("Error al eliminar ciclo: ${e.message}")
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
    fun obtenerCicloPorAnio(anio: Int): Collection<Ciclo> {
        tryConnection()

        var rs: ResultSet? = null
        val coleccion = mutableListOf<Ciclo>()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{ ? = call MOVILES.OBTENER_CICLO_POR_ANIO(?) }")
            pstmt?.registerOutParameter(1, OracleTypes.CURSOR) // Tipo REF CURSOR
            pstmt?.setInt(2, anio) // P_ANIO
            pstmt?.execute()

            rs = pstmt?.getObject(1) as ResultSet?

            while (rs?.next() == true) {
                coleccion.add(
                    Ciclo(
                        rs.getInt("ciclo_id"),
                        rs.getInt("anio"),
                        rs.getInt("numero"),
                        rs.getDate("fecha_inicio"),
                        rs.getDate("fecha_fin"),
                        rs.getInt("activo") == 1 // Convertir NUMBER (1/0) a boolean
                    )
                )
            }
        } catch (e: SQLException) {
            throw GlobalException("Error al obtener ciclo por año: ${e.message}")
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
            throw NoDataException("No se encontraron ciclos para el año $anio")
        }
    }
}