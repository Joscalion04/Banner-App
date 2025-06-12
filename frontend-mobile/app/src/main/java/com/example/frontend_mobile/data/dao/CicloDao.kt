package com.example.frontend_mobile.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.frontend_mobile.data.model.Ciclo

@Dao
interface CicloDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCiclo(ciclo: Ciclo)

    @Query("SELECT * FROM ciclos")
    suspend fun obtenerCiclos(): List<Ciclo>

    @Update
    suspend fun actualizarCiclo(ciclo: Ciclo)

    @Delete
    suspend fun eliminarCiclo(ciclo: Ciclo)
}