/*
    * @author: Derek Rojas Mendoza
    * @author: Joseph León Cabezas
*/

package com.example.backend.service

import com.example.backend.dao.*
import com.example.backend.model.*
import com.example.backend.webconfig.SocketHandler
import org.springframework.stereotype.Service

@Service
class Service(
    private val socketHandler: SocketHandler,
    private val daoMatricula: DaoMatricula,
    private val daoCarreras: DaoCarreras,
    private val daoCarreraCurso: DaoCarreraCurso,
    private val daoCursos: DaoCursos,
    private val daoProfesores: DaoProfesores,
    private val daoAlumnos: DaoAlumnos,
    private val daoCiclos: DaoCiclos,
    private val daoGrupos: DaoGrupos,
    private val daoUsuarios: DaoUsuario
) : I_Service {

    // ----------------- CRUD para Carrera -----------------

    override fun insertarCarrera(carrera: Carrera) {
        try {
            daoCarreras.insertarCarrera(carrera)
            socketHandler.notificarCambio("carrera", "insertar", carrera.getCodigoCarrera())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerCarreras(): Collection<Carrera> {
        try {
            return daoCarreras.obtenerCarreras()
        } catch (e: Exception) {
            throw e
        }
    }

    override fun actualizarCarrera(carrera: Carrera) {
        try {
            daoCarreras.actualizarCarrera(carrera)
            socketHandler.notificarCambio("carrera", "actualizar", carrera.getCodigoCarrera())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun eliminarCarrera(codigo: String) {
        try {
            daoCarreras.eliminarCarrera(codigo)
            socketHandler.notificarCambio("carrera", "eliminar", codigo)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerCarreraPorCodigo(codigo: String): Carrera? {
        try {
            return daoCarreras.obtenerCarreraPorCodigo(codigo)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerCarreraPorNombre(nombre: String): Collection<Carrera> {
        try {
            return daoCarreras.obtenerCarreraPorNombre(nombre)
        } catch (e: Exception) {
            throw e
        }
    }

    // ----------------- CRUD para Curso -----------------

    override fun insertarCurso(curso: Curso) {
        try {
            daoCursos.insertarCurso(curso)
            socketHandler.notificarCambio("curso", "insertar", curso.getCodigoCurso())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerCursos(): Collection<Curso> {
        try {
            return daoCursos.obtenerCursos()
        } catch (e: Exception) {
            throw e
        }
    }

    override fun actualizarCurso(curso: Curso) {
        try {
            daoCursos.actualizarCurso(curso)
            socketHandler.notificarCambio("curso", "actualizar", curso.getCodigoCurso())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun eliminarCurso(codigo: String) {
        try {
            daoCursos.eliminarCurso(codigo)
            socketHandler.notificarCambio("curso", "eliminar", codigo)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerCursoPorCodigo(codigo: String): Curso? {
        try {
            return daoCursos.obtenerCursoPorCodigo(codigo)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerCursosPorCarrera(codigoCarrera: String): Collection<Curso> {
        try {
            return daoCursos.obtenerCursosPorCarrera(codigoCarrera)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerCursosPorNombre(nombre: String): Collection<Curso> {
        try {
            return daoCursos.obtenerCursosPorNombre(nombre)
        } catch (e: Exception) {
            throw e
        }
    }

    // ----------------- CRUD para Carrera_Curso -----------------

    override fun insertarCarreraCurso(carreraCurso: CarreraCurso) {
        try {
            daoCarreraCurso.insertarCarreraCurso(carreraCurso)
            socketHandler.notificarCambio("carreraCurso", "insertar", carreraCurso.getCarreraCursoId().toString())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerCarrerasCursos(): Collection<CarreraCurso> {
        try {
            return daoCarreraCurso.obtenerCarrerasCursos()
        } catch (e: Exception) {
            throw e
        }
    }

    override fun actualizarCarreraCurso(carreraCurso: CarreraCurso) {
        try {
            daoCarreraCurso.actualizarCarreraCurso(carreraCurso)
            socketHandler.notificarCambio("carreraCurso", "actualizar", carreraCurso.getCarreraCursoId().toString())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun eliminarCarreraCurso(carreraCursoId: Int) {
        try {
            daoCarreraCurso.eliminarCarreraCurso(carreraCursoId)
            socketHandler.notificarCambio("carreraCurso", "eliminar", carreraCursoId.toString())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerCarreraCursoPorId(carreraCursoId: Int): CarreraCurso? {
        try {
            return daoCarreraCurso.obtenerCarreraCursoPorId(carreraCursoId)
        } catch (e: Exception) {
            throw e
        }
    }

    // ----------------- CRUD para Profesor -----------------

    override fun insertarProfesor(profesor: Profesor) {
        try {
            daoProfesores.insertarProfesor(profesor)
            daoUsuarios.insertarUsuario(Usuario(profesor.getCedula(), profesor.getCedula(), Usuario.TIPO_PROFESOR))
            socketHandler.notificarCambio("profesor", "insertar", profesor.getCedula())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerProfesores(): Collection<Profesor> {
        try {
            return daoProfesores.obtenerProfesores()
        } catch (e: Exception) {
            throw e
        }
    }

    override fun actualizarProfesor(profesor: Profesor) {
        try {
            daoProfesores.actualizarProfesor(profesor)
            socketHandler.notificarCambio("profesor", "actualizar", profesor.getCedula())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun eliminarProfesor(cedula: String) {
        try {
            daoProfesores.eliminarProfesor(cedula)
            socketHandler.notificarCambio("profesor", "eliminar", cedula)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerProfesorPorCedula(cedula: String): Profesor? {
        try {
            return daoProfesores.obtenerProfesorPorCedula(cedula)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerProfesorPorNombre(nombre: String): Collection<Profesor> {
        try {
            return daoProfesores.obtenerProfesorPorNombre(nombre)
        } catch (e: Exception) {
            throw e
        }
    }

    // ----------------- CRUD para Alumno -----------------

    override fun insertarAlumno(alumno: Alumno) {
        try {
            daoAlumnos.insertarAlumno(alumno)
            daoUsuarios.insertarUsuario(Usuario(alumno.getCedula(), alumno.getCedula(), Usuario.TIPO_ALUMNO))
            socketHandler.notificarCambio("alumno", "insertar", alumno.getCedula())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerAlumnos(): Collection<Alumno> {
        try {
            return daoAlumnos.obtenerAlumnos()
        } catch (e: Exception) {
            throw e
        }
    }

    override fun actualizarAlumno(alumno: Alumno) {
        try {
            daoAlumnos.actualizarAlumno(alumno)
            socketHandler.notificarCambio("alumno", "actualizar", alumno.getCedula())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun eliminarAlumno(cedula: String) {
        try {
            daoAlumnos.eliminarAlumno(cedula)
            socketHandler.notificarCambio("alumno", "eliminar", cedula)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerAlumnoPorCedula(cedula: String): Alumno? {
        try {
            return daoAlumnos.obtenerAlumnoPorCedula(cedula)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerAlumnoPorNombre(nombre: String): Collection<Alumno> {
        try {
            return daoAlumnos.obtenerAlumnoPorNombre(nombre)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerAlumnosPorCarrera(codigoCarrera: String): Collection<Alumno> {
        try {
            return daoAlumnos.obtenerAlumnosPorCarrera(codigoCarrera)
        } catch (e: Exception) {
            throw e
        }
    }

    // ----------------- CRUD para Ciclo -----------------

    override fun insertarCiclo(ciclo: Ciclo) {
        try {
            daoCiclos.insertarCiclo(ciclo)
            socketHandler.notificarCambio("ciclo", "insertar", ciclo.getCicloId().toString())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerCiclos(): Collection<Ciclo> {
        try {
            return daoCiclos.obtenerCiclos()
        } catch (e: Exception) {
            throw e
        }
    }

    override fun actualizarCiclo(ciclo: Ciclo) {
        try {
            daoCiclos.actualizarCiclo(ciclo)
            socketHandler.notificarCambio("ciclo", "actualizar", ciclo.getCicloId().toString())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun eliminarCiclo(anio: Int, cicloId: Int) {
        try {
            daoCiclos.eliminarCiclo(anio, cicloId)
            socketHandler.notificarCambio("ciclo", "eliminar", cicloId.toString())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerCicloPorAnio(anio: Int): Collection<Ciclo> {
        try {
            return daoCiclos.obtenerCicloPorAnio(anio)
        } catch (e: Exception) {
            throw e
        }
    }

    // ----------------- CRUD para Grupo -----------------

    override fun insertarGrupo(grupo: Grupo) {
        try {
            daoGrupos.insertarGrupo(grupo)
            socketHandler.notificarCambio("grupo", "insertar", grupo.getGrupoId().toString())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerGrupos(): Collection<Grupo> {
        try {
            return daoGrupos.obtenerGrupos()
        } catch (e: Exception) {
            throw e
        }
    }

    override fun actualizarGrupo(grupo: Grupo) {
        try {
            daoGrupos.actualizarGrupo(grupo)
            socketHandler.notificarCambio("grupo", "actualizar", grupo.getGrupoId().toString())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun eliminarGrupo(grupoId: Int) {
        try {
            daoGrupos.eliminarGrupo(grupoId)
            socketHandler.notificarCambio("grupo", "eliminar", grupoId.toString())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerGrupoPorId(grupoId: Int): Grupo? {
        try {
            return daoGrupos.obtenerGrupoPorId(grupoId)
        } catch (e: Exception) {
            throw e
        }
    }

    // ----------------- CRUD para Usuario -----------------

    override fun insertarUsuario(usuario: Usuario) {
        try {
            daoUsuarios.insertarUsuario(usuario)
            socketHandler.notificarCambio("usuario", "insertar", usuario.getCedula())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerUsuarios(): Collection<Usuario> {
        try {
            return daoUsuarios.obtenerUsuarios()
        } catch (e: Exception) {
            throw e
        }
    }

    override fun actualizarUsuario(usuario: Usuario) {
        try {
            daoUsuarios.actualizarUsuario(usuario)
            socketHandler.notificarCambio("usuario", "actualizar", usuario.getCedula())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun eliminarUsuario(cedula: String) {
        try {
            daoUsuarios.eliminarUsuario(cedula)
            socketHandler.notificarCambio("usuario", "eliminar", cedula)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun obtenerUsuarioPorCedula(cedula: String): Usuario? {
        try {
            return daoUsuarios.obtenerUsuarioPorCedula(cedula)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun login(cedula: String, clave: String): Usuario? {
        try {
            return daoUsuarios.login(cedula, clave)
        } catch (e: Exception) {
            throw e
        }
    }

    // ----------------- Matrícula -----------------

    override fun registrarMatricula(grupoId: Int, cedulaAlumno: String) {
        try {
            val historial = daoMatricula.consultarHistorialAcademico(cedulaAlumno)
            if (historial.any { it.grupoId == grupoId }) {
                throw IllegalArgumentException("El alumno ya está matriculado en este grupo")
            }
            daoMatricula.registrarMatricula(grupoId, cedulaAlumno)
            socketHandler.notificarCambio("matricula", "insertar", "$grupoId-$cedulaAlumno")
        } catch (e: Exception) {
            throw e
        }
    }

    override fun eliminarMatricula(grupoId: Int, cedulaAlumno: String) {
        try {
            daoMatricula.eliminarMatricula(grupoId, cedulaAlumno)
            socketHandler.notificarCambio("matricula", "eliminar", "$grupoId-$cedulaAlumno")
        } catch (e: Exception) {
            throw e
        }
    }

    // ----------------- Registro de Notas -----------------

    override fun registrarNota(grupoId: Int, cedulaAlumno: String, nota: Int) {
        try {
            daoMatricula.registrarNota(grupoId, cedulaAlumno, nota)
            socketHandler.notificarCambio("nota", "insertar", "$grupoId-$cedulaAlumno")
        } catch (e: Exception) {
            throw e
        }
    }

    // ----------------- Historial Académico -----------------

    override fun consultarHistorialAcademico(cedulaAlumno: String): Collection<Matricula> {
        try {
            return daoMatricula.consultarHistorialAcademico(cedulaAlumno)
        } catch (e: Exception) {
            throw e
        }
    }
}