package com.example.frontend_mobile.data.repository

import android.content.Context
import com.example.frontend_mobile.data.AppDatabase
import com.example.frontend_mobile.data.dao.AlumnoDao
import com.example.frontend_mobile.data.dao.CarreraDao
import com.example.frontend_mobile.data.dao.UsuarioDao
import com.example.frontend_mobile.data.model.Alumno
import com.example.frontend_mobile.data.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.forEach


object AlumnoRepository {
    private lateinit var alumnoDao: AlumnoDao
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var carreraDao: CarreraDao

    fun init(context: Context) {
        val db = AppDatabase.getDatabase(context)
        alumnoDao = db.alumnoDao()
        usuarioDao = db.usuarioDao()
        carreraDao = db.carreraDao()
    }

    suspend fun agregarAlumno(alumno: Alumno): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            if (carreraDao.obtenerCarreras().find { carrera -> carrera.codigoCarrera == alumno.codigoCarrera } == null) {
                throw IllegalArgumentException("Carrera no encontrada: ${alumno.codigoCarrera}")
            }
            alumnoDao.insertarAlumno(alumno)
            usuarioDao.insertarUsuario(Usuario(alumno.cedula, alumno.cedula, "ALUMNO"))
            true
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun listarAlumnos(): List<Alumno> = withContext(Dispatchers.IO) {
        return@withContext try {
            alumnoDao.obtenerAlumnos()
        } catch (e: Exception) {
            println("Error al listar alumnos: ${e.message}")
            emptyList()
        }
    }

    suspend fun setAlumnos(alumnos: List<Alumno>): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            alumnos.forEach {
                if (carreraDao.obtenerCarreras().find { carrera -> carrera.codigoCarrera == it.codigoCarrera } == null) {
                    throw IllegalArgumentException("Carrera no encontrada: ${it.codigoCarrera}")
                }
                alumnoDao.actualizarAlumno(it)
            }
            true
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun eliminarAlumno(alumno: Alumno): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            alumnoDao.eliminarAlumno(alumno)
            true
        } catch (e: Exception) {
            println("Error al eliminar alumno: ${e.message}")
            false
        }
    }
}
