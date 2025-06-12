package com.example.frontend_mobile.data.repository

import android.content.Context
import com.example.frontend_mobile.data.AppDatabase
import com.example.frontend_mobile.data.dao.MatriculaDao
import com.example.frontend_mobile.data.model.Matricula
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MatriculaRepository {
    private lateinit var matriculaDao: MatriculaDao

    fun init(context: Context) {
        val db = AppDatabase.getDatabase(context)
        matriculaDao = db.matriculaDao()
    }

    suspend fun agregarMatricula(matricula: Matricula): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            matriculaDao.insertarMatricula(matricula)
            true
        } catch (e: Exception) {
            println("Error al agregar matricula: ${e.message}")
            false
        }
    }

    suspend fun registrarNota(matricula: Matricula): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            matriculaDao.actualizarMatricula(matricula)
            true
        } catch (e: Exception) {
            println("Error al actualizar matricula: ${e.message}")
            false
        }
    }

    suspend fun eliminarMatricula(matricula: Matricula): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            matriculaDao.eliminarMatricula(matricula)
            true
        } catch (e: Exception) {
            println("Error al eliminar matricula: ${e.message}")
            false
        }
    }

}
