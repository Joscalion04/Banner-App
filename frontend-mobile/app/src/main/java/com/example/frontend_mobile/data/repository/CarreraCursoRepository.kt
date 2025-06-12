package com.example.frontend_mobile.data.repository

import android.content.Context
import com.example.frontend_mobile.data.AppDatabase
import com.example.frontend_mobile.data.dao.CarreraCursoDao
import com.example.frontend_mobile.data.model.CarreraCurso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.forEach


object CarreraCursoRepository {
    private lateinit var carreraCursoDao: CarreraCursoDao

    fun init(context: Context) {
        val db = AppDatabase.getDatabase(context)
        carreraCursoDao = db.carreraCursoDao()
    }

    suspend fun agregarCarreraCurso(carreraCurso: CarreraCurso): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            carreraCursoDao.insertarCarreraCurso(carreraCurso)
            true
        } catch (e: Exception) {
            println("Error al agregar carreraCurso: ${e.message}")
            false
        }
    }

    suspend fun listarCarrerasCursos(): List<CarreraCurso> = withContext(Dispatchers.IO) {
        return@withContext try {
            carreraCursoDao.obtenerCarreraCursos()
        } catch (e: Exception) {
            println("Error al listar carrerasCursos: ${e.message}")
            emptyList()
        }
    }

    suspend fun setCarrerasCursos(carrerasCursos: List<CarreraCurso>): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            carrerasCursos.forEach { carreraCursoDao.actualizarCarreraCurso(it) }
            true
        } catch (e: Exception) {
            println("Error al actualizar cursos por carrera: ${e.message}")
            false
        }
    }
}