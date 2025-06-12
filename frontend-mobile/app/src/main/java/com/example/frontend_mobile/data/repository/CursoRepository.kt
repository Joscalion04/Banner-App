package com.example.frontend_mobile.data.repository

import android.content.Context
import com.example.frontend_mobile.data.AppDatabase
import com.example.frontend_mobile.data.dao.CursoDao
import com.example.frontend_mobile.data.model.Curso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CursoRepository {
    private lateinit var cursoDao: CursoDao

    fun init(context: Context) {
        val db = AppDatabase.getDatabase(context)
        cursoDao = db.cursoDao()
    }

    suspend fun agregarCursoRemoto(curso: Curso): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            cursoDao.insertarCurso(curso)
            true
        } catch (e: Exception) {
            println("Error al agregar curso: ${e.message}")
            false
        }
    }

    suspend fun listarCursos(): List<Curso> = withContext(Dispatchers.IO) {
        return@withContext try {
            cursoDao.obtenerCursos()
        } catch (e: Exception) {
            println("Error al listar cursos: ${e.message}")
            emptyList()
        }
    }

    suspend fun generarCodigoCurso(): String {
        val cursos = listarCursos()
        val ultimoCodigo = cursos
            .mapNotNull { it.codigoCurso.toIntOrNull() }
            .maxOrNull() ?: 0
        return (ultimoCodigo + 1).toString().padStart(4, '0')
    }

    suspend fun setCursos(cursos: List<Curso>): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            cursos.forEach { cursoDao.actualizarCurso(it) }
            true
        } catch (e: Exception) {
            println("Error al actualizar cursos: ${e.message}")
            false
        }
    }

    suspend fun eliminarCurso(curso: Curso): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            cursoDao.eliminarCurso(curso)
            true
        } catch (e: Exception) {
            println("Error al eliminar curso: ${e.message}")
            false
        }
    }
}
