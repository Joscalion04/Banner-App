package com.example.frontend_mobile.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.frontend_mobile.data.model.Alumno

@Dao
interface AlumnoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarAlumno(alumno: Alumno)

    @Query("SELECT * FROM alumnos")
    suspend fun obtenerAlumnos(): List<Alumno>

    @Update
    suspend fun actualizarAlumno(alumno: Alumno)

    @Delete
    suspend fun eliminarAlumno(alumno: Alumno)
}