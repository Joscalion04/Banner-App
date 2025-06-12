package com.example.frontend_mobile.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.frontend_mobile.data.model.Profesor

@Dao
interface ProfesorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProfesor(profesor: Profesor)

    @Query("SELECT * FROM profesores")
    suspend fun obtenerProfesores(): List<Profesor>

    @Update
    suspend fun actualizarProfesor(profesor: Profesor)

    @Delete
    suspend fun eliminarProfesor(profesor: Profesor)
}