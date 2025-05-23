package com.example.backend.dao

import com.example.backend.exceptions.GlobalException

import com.example.backend.exceptions.NoDataException
import com.example.backend.model.*
import org.hibernate.dialect.OracleTypes
import org.springframework.stereotype.Repository
import java.sql.*
import kotlin.jvm.Throws

@Repository
class DaoCarreraCurso : Dao(){

    private fun tryConnection() {
        try {
            conectar()
        } catch (e: ClassNotFoundException) {
            throw GlobalException("No se ha localizado el driver")
        } catch (e: SQLException) {
            throw NoDataException("La base de datos no se encuentra disponible")
        }
    }

    // ----------------- CRUD para Carrera_Curso -----------------

    @Throws(GlobalException::class, NoDataException::class)
    fun insertarCarreraCurso(carreraCurso: CarreraCurso) {
        tryConnection()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{call MOVILES.INSERTAR_CARRERA_CURSO(?, ?, ?, ?, ?)}")
            pstmt?.setString(1, carreraCurso.getCodigoCarrera())
            pstmt?.setString(2, carreraCurso.getCodigoCurso())
            pstmt?.setInt(3, carreraCurso.getAnio())
            pstmt?.setInt(4, carreraCurso.getCiclo())
            pstmt?.setInt(5, carreraCurso.getOrden())

            val resultado = pstmt?.executeUpdate()
            if (resultado == 0) {
                throw NoDataException("No se realizó la inserción del curso en la carrera")
            }
        } catch (e: SQLException) {
            when (e.errorCode) {
                1 -> throw GlobalException("Ya existe esta combinación de carrera y curso")
                2291 -> throw GlobalException("Violación de integridad referencial (carrera o curso no existe)")
                else -> throw GlobalException("Error al insertar relación carrera-curso: ${e.message}")
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
    fun obtenerCarrerasCursos(): Collection<CarreraCurso> {
        tryConnection()

        var rs: ResultSet? = null
        val coleccion = mutableListOf<CarreraCurso>()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{? = call MOVILES.OBTENER_CARRERAS_CURSOS()}")
            pstmt?.registerOutParameter(1, OracleTypes.CURSOR)
            pstmt?.execute()
            rs = pstmt?.getObject(1) as ResultSet?

            while (rs?.next() == true) {
                coleccion.add(
                    CarreraCurso(
                        rs.getInt("carrera_curso_id"),
                        rs.getString("codigo_carrera"),
                        rs.getString("codigo_curso"),
                        rs.getInt("anio"),
                        rs.getInt("ciclo"),
                        rs.getInt("orden")
                    )
                )
            }
        } catch (e: SQLException) {
            throw GlobalException("Error al obtener relaciones carrera-curso: ${e.message}")
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
            throw NoDataException("No se encontraron relaciones carrera-curso registradas")
        }
    }

    @Throws(GlobalException::class, NoDataException::class)
    fun actualizarCarreraCurso(carreraCurso: CarreraCurso) {
        tryConnection()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{call MOVILES.ACTUALIZAR_CARRERA_CURSO(?, ?, ?, ?)}")
            pstmt?.setInt(1, carreraCurso.getCarreraCursoId())
            pstmt?.setInt(2, carreraCurso.getAnio())
            pstmt?.setInt(3, carreraCurso.getCiclo())
            pstmt?.setInt(4, carreraCurso.getOrden())

            val resultado = pstmt?.executeUpdate()
            if (resultado == 0) {
                throw NoDataException("No se realizó la actualización: relación carrera-curso no encontrada")
            }
        } catch (e: SQLException) {
            throw GlobalException("Error al actualizar relación carrera-curso: ${e.message}")
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
    fun eliminarCarreraCurso(carreraCursoId: Int) {
        tryConnection()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{call MOVILES.ELIMINAR_CARRERA_CURSO(?)}")
            pstmt?.setInt(1, carreraCursoId)

            val resultado = pstmt?.executeUpdate()
            if (resultado == 0) {
                throw NoDataException("No se realizó la eliminación: relación carrera-curso no encontrada")
            }
        } catch (e: SQLException) {
            if (e.errorCode == 2292) {
                throw GlobalException("No se puede eliminar la relación, tiene registros dependientes")
            }
            throw GlobalException("Error al eliminar relación carrera-curso: ${e.message}")
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
    fun obtenerCarreraCursoPorId(carreraCursoId: Int): CarreraCurso {
        tryConnection()

        var rs: ResultSet? = null
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{? = call MOVILES.OBTENER_CARRERA_CURSO_POR_ID(?)}")
            pstmt?.registerOutParameter(1, OracleTypes.CURSOR)
            pstmt?.setInt(2, carreraCursoId)
            pstmt?.execute()

            rs = pstmt?.getObject(1) as ResultSet?

            if (rs?.next() == true) {
                return CarreraCurso(
                    rs.getInt("carrera_curso_id"),
                    rs.getString("codigo_carrera"),
                    rs.getString("codigo_curso"),
                    rs.getInt("anio"),
                    rs.getInt("ciclo"),
                    rs.getInt("orden")
                )
            } else {
                throw NoDataException("No se encontró la relación carrera-curso con ID $carreraCursoId")
            }
        } catch (e: SQLException) {
            throw GlobalException("Error al obtener relación carrera-curso por ID: ${e.message}")
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

}