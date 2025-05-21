package com.example.backend.dao

import com.example.backend.exceptions.GlobalException
import com.example.backend.exceptions.NoDataException
import com.example.backend.model.*
import org.hibernate.dialect.OracleTypes
import org.springframework.stereotype.Repository
import java.sql.*
import kotlin.jvm.Throws

@Repository
class DaoUsuario : Dao(){

    private fun tryConnection() {
        try {
            conectar()
        } catch (e: ClassNotFoundException) {
            throw GlobalException("No se ha localizado el driver")
        } catch (e: SQLException) {
            throw NoDataException("La base de datos no se encuentra disponible")
        }
    }

    // ----------------- CRUD para Usuario -----------------

    @Throws(GlobalException::class, NoDataException::class)
    fun insertarUsuario(usuario: Usuario) {
        tryConnection()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{call MOVILES.INSERTAR_USUARIO(?, ?, ?)}")
            pstmt?.setString(1, usuario.getCedula())
            pstmt?.setString(2, usuario.getClave())
            pstmt?.setString(3, usuario.getTipoUsuario())
            pstmt?.execute()
        } catch (e: SQLException) {
            when (e.errorCode) {
                1 -> throw GlobalException("Ya existe un usuario con esta cédula")
                else -> throw GlobalException("Error al insertar usuario: ${e.message}")
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
    fun obtenerUsuarios(): Collection<Usuario> {
        tryConnection()

        var rs: ResultSet? = null
        val coleccion = mutableListOf<Usuario>()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{? = call MOVILES.OBTENER_USUARIOS()}")
            pstmt?.registerOutParameter(1, OracleTypes.CURSOR) // ✅ Usar OracleTypes.CURSOR directamente
            pstmt?.execute()
            rs = pstmt?.getObject(1) as ResultSet? // ✅ Cast directo a ResultSet

            while (rs?.next() == true) {
                coleccion.add(
                    Usuario(
                        rs.getString("cedula"),
                        rs.getString("clave"),
                        rs.getString("tipo_usuario")
                    )
                )
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw GlobalException("Error ejecutando obtenerUsuarios: ${e.message}")
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
    fun actualizarUsuario(usuario: Usuario) {
        tryConnection()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{call MOVILES.ACTUALIZAR_USUARIO(?, ?, ?)}")
            pstmt?.setString(1, usuario.getCedula())
            pstmt?.setString(2, usuario.getClave())
            pstmt?.setString(3, usuario.getTipoUsuario())

            val resultado = pstmt?.executeUpdate()
            if (resultado == 0) {
                throw NoDataException("No se realizó la actualización: usuario no encontrado")
            }
        } catch (e: SQLException) {
            throw GlobalException("Error al actualizar usuario: ${e.message}")
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
    fun eliminarUsuario(cedula: String) {
        tryConnection()
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{call MOVILES.ELIMINAR_USUARIO(?)}")
            pstmt?.setString(1, cedula)

            val resultado = pstmt?.executeUpdate()
            if (resultado == 0) {
                throw NoDataException("No se realizó la eliminación: usuario no encontrado")
            }
        } catch (e: SQLException) {
            if (e.errorCode == 2292) {
                throw GlobalException("No se puede eliminar el usuario, tiene registros dependientes")
            }
            throw GlobalException("Error al eliminar usuario: ${e.message}")
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
    fun obtenerUsuarioPorCedula(cedula: String): Usuario {
        tryConnection()

        var rs: ResultSet? = null
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{? = call MOVILES.OBTENER_USUARIO_POR_CEDULA(?)}")
            pstmt?.registerOutParameter(1, OracleTypes.CURSOR)
            pstmt?.setString(2, cedula)
            pstmt?.execute()

            rs = pstmt?.getObject(1) as ResultSet?

            if (rs?.next() == true) {
                return Usuario(
                    rs.getString("cedula"),
                    rs.getString("clave"),
                    rs.getString("tipo_usuario")
                )
            } else {
                throw NoDataException("No se encontró usuario con cédula $cedula")
            }
        } catch (e: SQLException) {
            throw GlobalException("Error al obtener usuario por cédula: ${e.message}")
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
    fun login(cedula: String, clave: String): Usuario {
        tryConnection()

        var rs: ResultSet? = null
        var pstmt: CallableStatement? = null

        try {
            pstmt = conexion?.prepareCall("{call MOVILES.LOGIN_USUARIO(?, ?, ?)}")
            pstmt?.setString(1, cedula)
            pstmt?.setString(2, clave)
            pstmt?.registerOutParameter(3, OracleTypes.CURSOR)

            pstmt?.execute()

            rs = pstmt?.getObject(3) as ResultSet?

            if (rs?.next() == true) {
                return Usuario(
                    rs.getString("cedula"),
                    rs.getString("clave"),
                    rs.getString("tipo_usuario")
                )
            } else {
                throw NoDataException("Credenciales incorrectas")
            }
        } catch (e: SQLException) {
            throw GlobalException("Error en login: ${e.message}")
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