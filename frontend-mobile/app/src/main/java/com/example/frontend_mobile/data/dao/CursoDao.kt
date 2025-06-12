package com.example.frontend_mobile.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.frontend_mobile.data.model.Curso

@Dao
interface CursoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCurso(curso: Curso)

    @Query("SELECT * FROM cursos")
    suspend fun obtenerCursos(): List<Curso>

    @Update
    suspend fun actualizarCurso(curso: Curso)

    @Delete
    suspend fun eliminarCurso(curso: Curso)
}