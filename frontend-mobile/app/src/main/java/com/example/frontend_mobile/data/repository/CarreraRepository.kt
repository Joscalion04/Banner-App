package com.example.frontend_mobile.data.repository

import android.content.Context
import com.example.frontend_mobile.data.AppDatabase
import com.example.frontend_mobile.data.dao.CarreraDao
import com.example.frontend_mobile.data.model.Carrera
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.forEach


object CarreraRepository {
    private lateinit var carreraDao: CarreraDao

    fun init(context: Context) {
        val db = AppDatabase.getDatabase(context)
        carreraDao = db.carreraDao()
    }

    suspend fun agregarCarrera(carrera: Carrera): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            carreraDao.insertarCarrera(carrera)
            true
        } catch (e: Exception) {
            println("Error al agregar la carrera: ${e.message}")
            false
        }
    }

    suspend fun listarCarreras(): List<Carrera> = withContext(Dispatchers.IO) {
        return@withContext try {
            carreraDao.obtenerCarreras()
        } catch (e: Exception) {
            println("Error al listar carreras: ${e.message}")
            emptyList()
        }
    }

    suspend fun setCarreras(carreras: List<Carrera>): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            carreras.forEach { carreraDao.actualizarCarrera(it) }
            true
        } catch (e: Exception) {
            println("Error al actualizar carreras: ${e.message}")
            false
        }
    }

    suspend fun eliminarCarrera(carrera: Carrera): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            carreraDao.eliminarCarrera(carrera)
            true
        } catch (e: Exception) {
            println("Error al eliminar la carrera: ${e.message}")
            false
        }
    }
}