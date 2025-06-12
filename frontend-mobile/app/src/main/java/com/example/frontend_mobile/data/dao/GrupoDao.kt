package com.example.frontend_mobile.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.frontend_mobile.data.model.Grupo

@Dao
interface GrupoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarGrupo(grupo: Grupo)

    @Query("SELECT * FROM grupos")
    suspend fun obtenerGrupos(): List<Grupo>

    @Update
    suspend fun actualizarGrupo(grupo: Grupo)

    @Delete
    suspend fun eliminarGrupo(grupo: Grupo)
}