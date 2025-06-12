package com.example.frontend_mobile.data.repository

import android.content.Context
import com.example.frontend_mobile.data.AppDatabase
import com.example.frontend_mobile.data.dao.GrupoDao
import com.example.frontend_mobile.data.dao.ProfesorDao
import com.example.frontend_mobile.data.model.Grupo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GrupoRepository {
    private lateinit var grupoDao: GrupoDao
    private lateinit var profesorDao: ProfesorDao

    fun init(context: Context) {
        val db = AppDatabase.getDatabase(context)
        grupoDao = db.grupoDao()
        profesorDao = db.profesorDao()
    }
    
    suspend fun agregarGrupoRemoto(grupo: Grupo): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            profesorDao.obtenerProfesores().find { profesor -> profesor.cedula == grupo.cedulaProfesor }
                ?: throw IllegalArgumentException("Profesor no encontrado: ${grupo.cedulaProfesor}")
            grupoDao.insertarGrupo(grupo)
            true
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun listarGrupos(): List<Grupo> = withContext(Dispatchers.IO) {
        return@withContext try {
            grupoDao.obtenerGrupos()
        } catch (e: Exception) {
            println("Error al listar grupos: ${e.message}")
            emptyList()
        }
    }

    suspend fun setGrupos(grupos: List<Grupo>): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            grupos.forEach {
                profesorDao.obtenerProfesores().find { profesor -> profesor.cedula == it.cedulaProfesor }
                    ?: throw IllegalArgumentException("Profesor no encontrado: ${it.cedulaProfesor}")
                grupoDao.actualizarGrupo(it)
            }
            true
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun eliminarGrupo(grupo: Grupo): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            grupoDao.eliminarGrupo(grupo)
            true
        } catch (e: Exception) {
            println("Error al eliminar grupo: ${e.message}")
            false
        }
    }
}
