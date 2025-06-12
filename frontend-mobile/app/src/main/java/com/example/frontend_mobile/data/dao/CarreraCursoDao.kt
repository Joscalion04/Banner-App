package com.example.frontend_mobile.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.frontend_mobile.data.model.CarreraCurso

@Dao
interface CarreraCursoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCarreraCurso(carreraCurso: CarreraCurso)

    @Query("SELECT * FROM carreras_cursos")
    suspend fun obtenerCarreraCursos(): List<CarreraCurso>

    @Update
    suspend fun actualizarCarreraCurso(carreraCurso: CarreraCurso)

    @Delete
    suspend fun eliminarCarreraCurso(carreraCurso: CarreraCurso)
}