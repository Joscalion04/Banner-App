package com.example.frontend_mobile.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.frontend_mobile.data.model.Matricula
import com.example.frontend_mobile.data.model.MatriculaRequest

@Dao
interface MatriculaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMatricula(matricula: Matricula)

    @Query("SELECT * FROM matriculas")
    suspend fun obtenerMatriculas(): List<Matricula>

    @Query("SELECT * FROM matriculas WHERE grupoId = :grupoId")
    suspend fun obtenerMatriculasPorGrupo(grupoId: Int): List<Matricula>

    @Query("SELECT * FROM matriculas WHERE cedulaAlumno = :cedula")
    suspend fun obtenerMatriculasPorAlumno(cedula: String): List<Matricula>

    @Update
    suspend fun actualizarMatricula(matricula: Matricula)

    @Delete
    suspend fun eliminarMatricula(matricula: Matricula)
}