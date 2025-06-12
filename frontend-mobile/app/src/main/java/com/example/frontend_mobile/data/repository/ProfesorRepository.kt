package com.example.frontend_mobile.data.repository

import android.content.Context
import com.example.frontend_mobile.data.AppDatabase
import com.example.frontend_mobile.data.dao.ProfesorDao
import com.example.frontend_mobile.data.dao.UsuarioDao
import com.example.frontend_mobile.data.model.Profesor
import com.example.frontend_mobile.data.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ProfesorRepository {
    private lateinit var profesorDao: ProfesorDao
    private lateinit var usuarioDao: UsuarioDao

    fun init(context: Context) {
        val db = AppDatabase.getDatabase(context)
        profesorDao = db.profesorDao()
        usuarioDao = db.usuarioDao()
    }

    suspend fun agregarProfesor(profesor: Profesor): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            profesorDao.insertarProfesor(profesor)
            usuarioDao.insertarUsuario(Usuario(profesor.cedula, profesor.cedula, "PROFESOR"))
            true
        } catch (e: Exception) {
            println("Error al agregar profesor: ${e.message}")
            false
        }
    }

    suspend fun listarProfesores(): List<Profesor> = withContext(Dispatchers.IO) {
        return@withContext try {
            profesorDao.obtenerProfesores()
        } catch (e: Exception) {
            println("Error al listar profesores: ${e.message}")
            emptyList()
        }
    }

    suspend fun setProfesores(profesores: List<Profesor>): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            profesores.forEach { profesorDao.actualizarProfesor(it) }
            true
        } catch (e: Exception) {
            println("Error al actualizar profesores: ${e.message}")
            false
        }
    }

    suspend fun eliminarProfesor(profesor: Profesor): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            profesorDao.eliminarProfesor(profesor)
            true
        } catch (e: Exception) {
            println("Error al eliminar profesor: ${e.message}")
            false
        }
    }

}
