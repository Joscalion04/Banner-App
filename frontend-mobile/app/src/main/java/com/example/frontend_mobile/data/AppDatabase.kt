package com.example.frontend_mobile.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.frontend_mobile.data.dao.AlumnoDao
import com.example.frontend_mobile.data.dao.CarreraCursoDao
import com.example.frontend_mobile.data.dao.CarreraDao
import com.example.frontend_mobile.data.dao.CicloDao
import com.example.frontend_mobile.data.dao.CursoDao
import com.example.frontend_mobile.data.dao.GrupoDao
import com.example.frontend_mobile.data.dao.MatriculaDao
import com.example.frontend_mobile.data.dao.ProfesorDao
import com.example.frontend_mobile.data.dao.UsuarioDao
import com.example.frontend_mobile.data.model.Alumno
import com.example.frontend_mobile.data.model.Carrera
import com.example.frontend_mobile.data.model.CarreraCurso
import com.example.frontend_mobile.data.model.Ciclo
import com.example.frontend_mobile.data.model.Curso
import com.example.frontend_mobile.data.model.Grupo
import com.example.frontend_mobile.data.model.Matricula
import com.example.frontend_mobile.data.model.Profesor
import com.example.frontend_mobile.data.model.Usuario

@Database(
    entities = [
        Alumno::class,
        CarreraCurso::class,
        Carrera::class,
        Ciclo::class,
        Curso::class,
        Grupo::class,
        Matricula::class,
        Profesor::class,
        Usuario::class
    ],
    version = 3
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun alumnoDao(): AlumnoDao
    abstract fun carreraCursoDao(): CarreraCursoDao
    abstract fun carreraDao(): CarreraDao
    abstract fun cicloDao(): CicloDao
    abstract fun cursoDao(): CursoDao
    abstract fun grupoDao(): GrupoDao
    abstract fun matriculaDao(): MatriculaDao
    abstract fun profesorDao(): ProfesorDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}