package com.example.frontend_mobile.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.frontend_mobile.data.model.Carrera

@Dao
interface CarreraDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCarrera(carrera: Carrera)

    @Query("SELECT * FROM carreras")
    suspend fun obtenerCarreras(): List<Carrera>

    @Update
    suspend fun actualizarCarrera(carrera: Carrera)

    @Delete
    suspend fun eliminarCarrera(carrera: Carrera)
}