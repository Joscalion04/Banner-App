/*
    * @author: Derek Rojas Mendoza
    * @author: Joseph León Cabezas
*/

package com.example.backend.service

import com.example.backend.model.*

interface I_Service {

    // ----------------- CRUD para Carrera -----------------

    fun insertarCarrera(carrera: Carrera)

    fun obtenerCarreras(): Collection<Carrera>

    fun actualizarCarrera(carrera: Carrera)

    fun eliminarCarrera(codigo: String)

    fun obtenerCarreraPorCodigo(codigo: String): Carrera?

    fun obtenerCarreraPorNombre(nombre: String): Collection<Carrera>

    // ----------------- CRUD para Curso -----------------

    fun insertarCurso(curso: Curso)

    fun obtenerCursos(): Collection<Curso>

    fun actualizarCurso(curso: Curso)

    fun eliminarCurso(codigo: String)

    fun obtenerCursoPorCodigo(codigo: String): Curso?

    fun obtenerCursosPorCarrera(codigoCarrera: String): Collection<Curso>

    fun obtenerCursosPorNombre(nombre: String): Collection<Curso>

    // ----------------- CRUD para Carrera_Curso -----------------

    fun insertarCarreraCurso(carreraCurso: CarreraCurso)

    fun obtenerCarrerasCursos(): Collection<CarreraCurso>

    fun actualizarCarreraCurso(carreraCurso: CarreraCurso)

    fun eliminarCarreraCurso(carreraCursoId: Int)

    fun obtenerCarreraCursoPorId(carreraCursoId: Int): CarreraCurso?

    // ----------------- CRUD para Profesor -----------------

    fun insertarProfesor(profesor: Profesor)

    fun obtenerProfesores(): Collection<Profesor>

    fun actualizarProfesor(profesor: Profesor)

    fun eliminarProfesor(cedula: String)

    fun obtenerProfesorPorCedula(cedula: String): Profesor?

    fun obtenerProfesorPorNombre(nombre: String): Collection<Profesor>

    // ----------------- CRUD para Alumno -----------------

    fun insertarAlumno(alumno: Alumno)

    fun obtenerAlumnos(): Collection<Alumno>

    fun actualizarAlumno(alumno: Alumno)

    fun eliminarAlumno(cedula: String)

    fun obtenerAlumnoPorCedula(cedula: String): Alumno?

    fun obtenerAlumnoPorNombre(nombre: String): Collection<Alumno>

    fun obtenerAlumnosPorCarrera(codigoCarrera: String): Collection<Alumno>

    // ----------------- CRUD para Ciclo -----------------

    fun insertarCiclo(ciclo: Ciclo)

    fun obtenerCiclos(): Collection<Ciclo>

    fun actualizarCiclo(ciclo: Ciclo)

    fun eliminarCiclo(anio: Int, cicloId: Int)

    fun obtenerCicloPorAnio(anio: Int): Collection<Ciclo>

    // ----------------- CRUD para Grupo -----------------

    fun insertarGrupo(grupo: Grupo)

    fun obtenerGrupos(): Collection<Grupo>

    fun actualizarGrupo(grupo: Grupo)

    fun eliminarGrupo(grupoId: Int)

    fun obtenerGrupoPorId(grupoId: Int): Grupo?

    // ----------------- CRUD para Usuario -----------------

    fun insertarUsuario(usuario: Usuario)

    fun obtenerUsuarios(): Collection<Usuario>

    fun actualizarUsuario(usuario: Usuario)

    fun eliminarUsuario(cedula: String)

    fun obtenerUsuarioPorCedula(cedula: String): Usuario?

    fun login(cedula: String, clave: String) : Usuario?

    // ----------------- Matrícula -----------------

    fun registrarMatricula(grupoId: Int, cedulaAlumno: String)

    fun eliminarMatricula(grupoId: Int, cedulaAlumno: String)

    // ----------------- Registro de Notas -----------------

    fun registrarNota(grupoId: Int, cedulaAlumno: String, nota: Int)

    // ----------------- Historial Académico -----------------

    fun consultarHistorialAcademico(cedulaAlumno: String): Collection<Matricula>
}