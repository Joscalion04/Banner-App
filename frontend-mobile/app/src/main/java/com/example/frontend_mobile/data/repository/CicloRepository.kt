package com.example.frontend_mobile.data.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.frontend_mobile.data.AppDatabase
import com.example.frontend_mobile.data.dao.CicloDao
import com.example.frontend_mobile.data.model.Ciclo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


object CicloRepository {
    private lateinit var cicloDao: CicloDao

    fun init(context: Context) {
        val db = AppDatabase.getDatabase(context)
        cicloDao = db.cicloDao()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun agregarCiclo(ciclo: Ciclo): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            if (ciclo.activo) {
                val cicloActivo = cicloDao.obtenerCiclos().find { it -> it.activo }
                if (cicloActivo != null) {
                    cicloActivo.activo = false
                    cicloDao.actualizarCiclo(cicloActivo)
                }
            }
            cicloDao.insertarCiclo(ciclo)
            true
        } catch (e: Exception) {
            println("Error al agregar ciclo: ${e.message}")
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun listarCiclos(): List<Ciclo> = withContext(Dispatchers.IO) {
        return@withContext try {
            cicloDao.obtenerCiclos()
        } catch (e: Exception) {
            println("Error al listar ciclos: ${e.message}")
            emptyList()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun setCiclos(ciclos: List<Ciclo>): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val cicloActivo = cicloDao.obtenerCiclos().find { ciclo -> ciclo.activo }
            if (cicloActivo != null) {
                cicloActivo.activo = false
                cicloDao.actualizarCiclo(cicloActivo)
            }
            ciclos.forEach {
                if (it.cicloId != cicloActivo?.cicloId) {
                    cicloDao.actualizarCiclo(it)
                }
            }
            true
        } catch (e: Exception) {
            println("Error al insertar ciclos: ${e.message}")
            false
        }
    }

    suspend fun eliminarCiclo(ciclo: Ciclo): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            cicloDao.eliminarCiclo(ciclo)
            true
        } catch (e: Exception) {
            println("Error al eliminar ciclo: ${e.message}")
            false
        }
    }
}