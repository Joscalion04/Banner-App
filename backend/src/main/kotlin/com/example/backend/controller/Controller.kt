/*
    * @author: Derek Rojas Mendoza
    * @author: Joseph León Cabezas
*/

package com.example.backend.controller

import com.example.backend.exceptions.GlobalException
import com.example.backend.exceptions.NoDataException
import com.example.backend.model.*
import com.example.backend.service.I_Service
import com.example.backend.service.Service
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class Controller {
    
    private val service: I_Service = Service()

    // ----------------- CRUD para Carrera -----------------
    @PostMapping("/insertarCarrera")
    fun insertarCarrera(@RequestBody carrera: Carrera): ResponseEntity<Any> {
        return try {
            service.insertarCarrera(carrera)
            ResponseEntity.ok("Carrera insertada correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al insertar carrera: ${e.message}")
        }
    }


    @GetMapping("/obtenerCarreras")
    fun obtenerCarreras(): Collection<Carrera> {
        try {
            return service.obtenerCarreras()
        } catch (e: Exception) {
            throw e
        }
    }

    @PostMapping("/actualizarCarrera")
    fun actualizarCarrera(@RequestBody carrera: Carrera): ResponseEntity<Any> {
        return try {
            service.actualizarCarrera(carrera)
            ResponseEntity.ok("Carrera actualizada correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar carrera: ${e.message}")
        }
    }


    @DeleteMapping("/eliminarCarrera/{codigo}")
    fun eliminarCarrera(@PathVariable codigo: String): ResponseEntity<Any> {
        return try {
            service.eliminarCarrera(codigo)
            ResponseEntity.ok("Carrera eliminada correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar carrera: ${e.message}")
        }
    }


    @GetMapping("/obtenerCarrera/code/{codigo}")
    fun obtenerCarreraPorCodigo(@PathVariable codigo: String): ResponseEntity<Any> {
        return try {
            val carrera = service.obtenerCarreraPorCodigo(codigo)
            ResponseEntity.ok(carrera)
        } catch (e: NoDataException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener carrera: ${e.message}")
        }
    }

    @GetMapping("/obtenerCarrera/name/{name}")
    fun obtenerCarreraPorNombre(@PathVariable name: String): ResponseEntity<Any> {
        return try {
            val carrera = service.obtenerCarreraPorNombre(name)
            ResponseEntity.ok(carrera)
        } catch (e: NoDataException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener carrera: ${e.message}")
        }
    }

    // ----------------- CRUD para Curso -----------------
    @PostMapping("/insertarCurso")
    fun insertarCurso(@RequestBody curso: Curso): ResponseEntity<Any> {
        return try {
            service.insertarCurso(curso)
            ResponseEntity.ok("Curso insertada correctamente")
        }  catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al insertar curso: ${e.message}")
        }
    }

    @GetMapping("/obtenerCursos")
    fun obtenerCursos(): Collection<Curso> {
        try {
            return service.obtenerCursos()
        } catch (e: Exception) {
            throw e
        }
    }

    @PostMapping("/actualizarCurso")
    fun actualizarCurso(@RequestBody curso: Curso): ResponseEntity<Any> {
        return try {
            service.actualizarCurso(curso)
            ResponseEntity.ok("Curso actualizada correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar Curso: ${e.message}")
        }
    }


    @DeleteMapping("/eliminarCurso/{codigo}")
    fun eliminarCurso(@PathVariable codigo: String): ResponseEntity<Any> {
        return try {
            service.eliminarCurso(codigo)
            ResponseEntity.ok("Curso eliminada correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar Curso: ${e.message}")
        }
    }


    @GetMapping("/obtenerCurso/code/{codigo}")
    fun obtenerCursoPorCodigo(@PathVariable codigo: String): ResponseEntity<Any> {
        return try {
            val curso = service.obtenerCursoPorCodigo(codigo)
            ResponseEntity.ok(curso)
        } catch (e: NoDataException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener carrera: ${e.message}")
        }
    }


    @GetMapping("/obtenerCurso/name/{name}")
    fun obtenerCursosPorNombre(@PathVariable name: String): ResponseEntity<Any> {
        return try {
            val curso = service.obtenerCursosPorNombre(name)
            ResponseEntity.ok(curso)
        } catch (e: NoDataException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener carrera: ${e.message}")
        }
    }

    @GetMapping("/obtenerCurso/carrera/{codigoCarrera}")
    fun obtenerCursosPorCarrera(@PathVariable codigoCarrera: String): ResponseEntity<Any> {
        return try {
            val curso = service.obtenerCursosPorCarrera(codigoCarrera)
            ResponseEntity.ok(curso)
        } catch (e: NoDataException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener carrera: ${e.message}")
        }
    }



    // ----------------- CRUD para Carrera_Curso -----------------

    @PostMapping("/insertarCarreraCurso")
    fun insertarCarreraCurso(@RequestBody carreraCurso: CarreraCurso): ResponseEntity<Any> {
        return try {
            service.insertarCarreraCurso(carreraCurso)
            ResponseEntity.ok("Relación Carrera-Curso insertada correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al insertar relación Carrera-Curso: ${e.message}")
        }
    }

    @GetMapping("/obtenerCarrerasCursos")
    fun obtenerCarrerasCursos(): Collection<CarreraCurso> {
        try {
            return service.obtenerCarrerasCursos()
        } catch (e: Exception) {
            throw e
        }
    }

    @PostMapping("/actualizarCarreraCurso")
    fun actualizarCarreraCurso(@RequestBody carreraCurso: CarreraCurso): ResponseEntity<Any> {
        return try {
            service.actualizarCarreraCurso(carreraCurso)
            ResponseEntity.ok("Relación Carrera-Curso actualizada correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar relación Carrera-Curso: ${e.message}")
        }
    }

    @DeleteMapping("/eliminarCarreraCurso/{carreraCursoId}")
    fun eliminarCarreraCurso(@PathVariable carreraCursoId: Int): ResponseEntity<Any> {
        return try {
            service.eliminarCarreraCurso(carreraCursoId)
            ResponseEntity.ok("Relación Carrera-Curso eliminada correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar relación Carrera-Curso: ${e.message}")
        }
    }

    @GetMapping("/obtenerCarreraCurso/{carreraCursoId}")
    fun obtenerCarreraCursoPorId(@PathVariable carreraCursoId: Int): ResponseEntity<Any> {
        return try {
            val carreraCurso = service.obtenerCarreraCursoPorId(carreraCursoId)
            ResponseEntity.ok(carreraCurso)
        } catch (e: NoDataException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al obtener relación Carrera-Curso: ${e.message}")
        }
    }

    // Puedes agregar más endpoints específicos si los necesitas, por ejemplo:
    @GetMapping("/obtenerCarrerasCursosPorCarrera/{codigoCarrera}")
    fun obtenerCarrerasCursosPorCarrera(@PathVariable codigoCarrera: String): ResponseEntity<Any> {
        return try {
            val cursos = service.obtenerCursosPorCarrera(codigoCarrera)
            ResponseEntity.ok(cursos)
        } catch (e: NoDataException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al obtener cursos por carrera: ${e.message}")
        }
    }
    // ----------------- CRUD para Profesor -----------------


    @PostMapping("/insertarProfesor")
    fun insertarProfesor(@RequestBody profesor: Profesor) : ResponseEntity<Any> {
        return try {
            service.insertarProfesor(profesor)
            ResponseEntity.ok("Profesor insertada correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al insertar Profesor: ${e.message}")
        }
    }

    @GetMapping("/obtenerProfesores")
    fun obtenerProfesores(): Collection<Profesor> {
        try {
            return service.obtenerProfesores()
        } catch (e: Exception) {
            throw e
        }
    }

    @PostMapping("/actualizarProfesor")
    fun actualizarProfesor(@RequestBody profesor: Profesor): ResponseEntity<Any> {
        return try {
            service.actualizarProfesor(profesor)
            ResponseEntity.ok("Profesor actualizada correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar Profesor: ${e.message}")
        }
    }

    @DeleteMapping("/eliminarProfesor/{cedula}")
    fun eliminarProfesor(@PathVariable cedula: String): ResponseEntity<Any> {
        return try {
            service.eliminarProfesor(cedula)
            ResponseEntity.ok("Profesor Eliminado correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar Profesor: ${e.message}")
        }
    }

    @GetMapping("/obtenerProfesor/id/{cedula}")
    fun obtenerProfesorPorCedula(@PathVariable cedula: String): ResponseEntity<Any> {
        return try {
            val profesor = service.obtenerProfesorPorCedula(cedula)
            ResponseEntity.ok(profesor)
        } catch (e: NoDataException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener profesor: ${e.message}")
        }
    }

    @GetMapping("/obtenerProfesor/name/{name}")
    fun obtenerProfesorPorNombre(@PathVariable name: String): ResponseEntity<Any> {
        return try {
            val profesor = service.obtenerProfesorPorNombre(name)
            ResponseEntity.ok(profesor)
        } catch (e: NoDataException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener profesor: ${e.message}")
        }
    }

    // ----------------- CRUD para Alumno -----------------

    @PostMapping("/insertarAlumno")
    fun insertarAlumno( @RequestBody alumno: Alumno)  : ResponseEntity<Any> {
        return try {
            service.insertarAlumno(alumno)
            ResponseEntity.ok("Alumno insertada correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al insertar Alumno: ${e.message}")
        }
    }

    @GetMapping("/obtenerAlumnos")
    fun obtenerAlumnos(): Collection<Alumno> {
        try {
            return service.obtenerAlumnos()
        } catch (e: Exception) {
            throw e
        }
    }

    @PostMapping("/actualizarAlumno")
    fun actualizarAlumno(@RequestBody alumno: Alumno) : ResponseEntity<Any> {
        return try {
            service.actualizarAlumno(alumno)
            ResponseEntity.ok("Alumno actualizada correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar Alumno: ${e.message}")
        }
    }

    @DeleteMapping("/eliminarAlumno/{cedula}")
    fun eliminarAlumno(@PathVariable cedula: String): ResponseEntity<Any> {
        return try {
            service.eliminarAlumno(cedula)
            ResponseEntity.ok("Alumn Eliminado correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar Alumn: ${e.message}")
        }
    }

    @GetMapping("/obtenerAlumno/cedula/{cedula}")
    fun obtenerAlumnoPorCedula(@PathVariable cedula: String): ResponseEntity<Any> {
        return try {
            val alumno = service.obtenerAlumnoPorCedula(cedula)
            ResponseEntity.ok(alumno)
        } catch (e: NoDataException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener Alumno: ${e.message}")
        }
    }

    @GetMapping("/obtenerAlumno/nombre/{nombre}")
    fun obtenerAlumnoPorNombre(@PathVariable nombre: String): ResponseEntity<Any> {
        return try {
            val alumno = service.obtenerAlumnoPorNombre(nombre)
            ResponseEntity.ok(alumno)
        } catch (e: NoDataException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener Alumno: ${e.message}")
        }
    }

    @GetMapping("/obtenerAlumno/carrera/{codigoCarrera}")
    fun obtenerAlumnosPorCarrera(@PathVariable codigoCarrera: String): ResponseEntity<Any> {
        return try {
            val alumno = service.obtenerAlumnosPorCarrera(codigoCarrera)
            ResponseEntity.ok(alumno)
        } catch (e: NoDataException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener Alumno: ${e.message}")
        }
    }

    // ----------------- CRUD para Ciclo -----------------

    @PostMapping("/insertarCiclo")
    fun insertarCiclo( @RequestBody ciclo: Ciclo) : ResponseEntity<Any> {
        return try {
            service.insertarCiclo(ciclo)
            ResponseEntity.ok("Ciclo insertado correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al insertar Ciclo: ${e.message}")
        }
    }

    @GetMapping("/obtenerCiclos")
    fun obtenerCiclos(): Collection<Ciclo> {
        try {
            return service.obtenerCiclos()
        } catch (e: Exception) {
            throw e
        }
    }

    @PostMapping("/actualizarCiclo")
    fun actualizarCiclo(@RequestBody ciclo: Ciclo) : ResponseEntity<Any> {
        return try {
            service.actualizarCiclo(ciclo)
            ResponseEntity.ok("Ciclo actualizado correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar Ciclo: ${e.message}")
        }
    }

    @DeleteMapping("/eliminarCiclo/{anio}/{numero}")
    fun eliminarCiclo(
        @PathVariable anio: Int,
        @PathVariable numero: Int
    ): ResponseEntity<String> {
        return try {
            service.eliminarCiclo(anio, numero)
            ResponseEntity.ok("Ciclo eliminado exitosamente")
        } catch (e: NoDataException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: GlobalException) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
    }

    @GetMapping("/obtenerCicloPorAnio/anio/{anio}")
    fun obtenerCicloPorAnio(@PathVariable anio: Int): ResponseEntity<Any> {
        return try {
            val ciclo = service.obtenerCicloPorAnio(anio)
            ResponseEntity.ok(ciclo)
        } catch (e: NoDataException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener Ciclo: ${e.message}")
        }
    }

    // ----------------- CRUD para Grupo -----------------

    @PostMapping("/insertarGrupo")
    fun insertarGrupo(@RequestBody grupo: Grupo) : ResponseEntity<Any> {
        return try {
            service.insertarGrupo(grupo)
            ResponseEntity.ok("Grupo insertado correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al insertar Grupo: ${e.message}")
        }
    }

    @GetMapping("/obtenerGrupos")
    fun obtenerGrupos(): Collection<Grupo> {
        try {
            return service.obtenerGrupos()
        } catch (e: Exception) {
            throw e
        }
    }

    @PostMapping("/actualizarGrupo")
    fun actualizarGrupo(@RequestBody grupo: Grupo) : ResponseEntity<Any> {
        return try {
            service.actualizarGrupo(grupo)
            ResponseEntity.ok("Grupo actualizado correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar Grupo: ${e.message}")
        }
    }

    @DeleteMapping("/eliminarGrupo/{grupoId}")
    fun eliminarGrupo(@PathVariable grupoId: Int) : ResponseEntity<Any> {
        return try {
            service.eliminarGrupo(grupoId)
            ResponseEntity.ok("Grupo Eliminado correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar Grupo: ${e.message}")
        }
    }


    @GetMapping("/obtenerGrupoPorId/{grupoId}")
    fun obtenerGrupoPorId(@PathVariable grupoId: Int): ResponseEntity<Any> {
        return try {
            val grupo =  service.obtenerGrupoPorId(grupoId)
            ResponseEntity.ok(grupo)
        } catch (e: NoDataException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener Grupo: ${e.message}")
        }
    }

    // ----------------- CRUD para Usuario -----------------

    @PostMapping("/insertarUsuario")
    fun insertarUsuario(@RequestBody usuario: Usuario) : ResponseEntity<Any> {
        return try {
            service.insertarUsuario(usuario)
            ResponseEntity.ok("Usuario insertado correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al insertar Usuario: ${e.message}")
        }
    }

    @GetMapping("/obtenerUsuarios")
    fun obtenerUsuarios(): Collection<Usuario> {
        try {
            return service.obtenerUsuarios()
        } catch (e: Exception) {
            throw e
        }
    }

    @PostMapping("/actualizarUsuario")
    fun actualizarUsuario(@RequestBody usuario: Usuario) : ResponseEntity<Any> {
        return try {
            service.actualizarUsuario(usuario)
            ResponseEntity.ok("Usuario actualizado correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar Usuario: ${e.message}")
        }
    }

    @DeleteMapping("/eliminarUsuario/{cedula}")
    fun eliminarUsuario(@PathVariable cedula: String) : ResponseEntity<Any> {
        return try {
            service.eliminarUsuario(cedula)
            ResponseEntity.ok("Usuario Eliminado correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar Usuario: ${e.message}")
        }
    }

    @GetMapping("/obtenerUsuarioPorCedula/{cedula}")
    fun obtenerUsuarioPorCedula(@PathVariable cedula: String): ResponseEntity<Any> {
        return try {
            val Usuario =  service.obtenerUsuarioPorCedula(cedula)
            ResponseEntity.ok(Usuario)
        }  catch (e: NoDataException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener Usuario: ${e.message}")
        }
    }

    @PostMapping("/login")
    fun loginUsuario(@RequestBody usuario: Usuario): ResponseEntity<Any> {
        return try {
            val usuarioLogueado = service.login(usuario.getCedula(), usuario.getClave())
            ResponseEntity.ok(usuarioLogueado)
        } catch (e: com.example.backend.exceptions.NoDataException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en login: ${e.message}")
        }
    }

    // ----------------- Matrícula -----------------

    data class MatriculaRequest(
        val grupoId: Int,
        val cedulaAlumno: String
    )

    @PostMapping("/registrarMatricula")
    fun registrarMatricula(@RequestBody request: MatriculaRequest): ResponseEntity<Any> {
        return try {
            service.registrarMatricula(request.grupoId, request.cedulaAlumno)
            ResponseEntity.ok("Matrícula registrada correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar matrícula: ${e.message}")
        }
    }

    @DeleteMapping("/eliminarMatricula")
    fun eliminarMatricula(@RequestBody request: MatriculaRequest): ResponseEntity<Any> {
        return try {
            service.eliminarMatricula(request.grupoId, request.cedulaAlumno)
            ResponseEntity.ok("Matrícula eliminada correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar matrícula: ${e.message}")
        }
    }

// ----------------- Registro de Notas -----------------

    data class NotaRequest(
        val grupoId: Int,
        val cedulaAlumno: String,
        val nota: Int
    )

    @PostMapping("/registrarNota")
    fun registrarNota(@RequestBody request: NotaRequest): ResponseEntity<Any> {
        return try {
            service.registrarNota(request.grupoId, request.cedulaAlumno, request.nota)
            ResponseEntity.ok("Nota registrada correctamente")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar nota: ${e.message}")
        }
    }

// ----------------- Historial Académico -----------------

    @GetMapping("/consultarHistorialAcademico/{cedulaAlumno}")
    fun consultarHistorialAcademico(@PathVariable cedulaAlumno: String): ResponseEntity<Any> {
        return try {
            val historial = service.consultarHistorialAcademico(cedulaAlumno)
            ResponseEntity.ok(historial)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al consultar historial académico: ${e.message}")
        }
    }

}