const API_BASE_URL = 'http://localhost:8080/api';

export const fetchApi = async (endpoint, options = {}) => {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        headers: {
            'Content-Type': 'application/json',
            ...options.headers
        },
        ...options
    });
    
    const textResponse = await response.text(); // Primero obtener como texto

    try {
        // Intentar parsear como JSON
        const data = textResponse ? JSON.parse(textResponse) : null;
        
        if (!response.ok) {
            const errorMsg = data?.message || textResponse || `Error ${response.status}`;
            throw new Error(errorMsg);
        }
        
        return data || { success: true, message: textResponse };
    } catch (e) {
        // Si falla el parseo JSON pero la respuesta fue exitosa
        if (response.ok) {
            return { success: true, message: textResponse };
        }
        throw new Error(textResponse || `Error ${response.status}`);
    }
};

// CRUD para Carreras (completo)
export const obtenerCarreras = async () => fetchApi('/obtenerCarreras');
export const obtenerCarreraPorCodigo = async (codigo) => fetchApi(`/obtenerCarrera/code/${codigo}`);
export const obtenerCarreraPorNombre = async (nombre) => fetchApi(`/obtenerCarrera/name/${nombre}`);
export const insertarCarrera = async (data) => fetchApi('/insertarCarrera', { 
  method: 'POST', 
  body: JSON.stringify(data) 
});
export const actualizarCarrera = async (data) => fetchApi('/actualizarCarrera', { 
  method: 'POST', 
  body: JSON.stringify(data) 
});
export const eliminarCarrera = async (codigo) => fetchApi(`/eliminarCarrera/${codigo}`, { 
  method: 'DELETE' 
});

// CRUD para Cursos
export const obtenerCursos = async () => fetchApi('/obtenerCursos');
export const obtenerCursoPorCodigo = async (codigo) => fetchApi(`/obtenerCurso/code/${codigo}`);
export const insertarCurso = async (data) => fetchApi('/insertarCurso', { method: 'POST', body: JSON.stringify(data) });
export const actualizarCurso = async (data) => fetchApi('/actualizarCurso', { method: 'POST', body: JSON.stringify(data) });
export const eliminarCurso = async (codigo) => fetchApi(`/eliminarCurso/${codigo}`, { method: 'DELETE' });

// CRUD para Profesores
export const obtenerProfesores = async () => fetchApi('/obtenerProfesores');
export const obtenerProfesorPorCedula = async (cedula) => fetchApi(`/obtenerProfesor/id/${cedula}`);
export const insertarProfesor = async (data) => fetchApi('/insertarProfesor', { method: 'POST', body: JSON.stringify(data) });
export const actualizarProfesor = async (data) => fetchApi('/actualizarProfesor', { method: 'POST', body: JSON.stringify(data) });
export const eliminarProfesor = async (cedula) => fetchApi(`/eliminarProfesor/${cedula}`, { method: 'DELETE' });

// CRUD para Alumnos
export const obtenerAlumnos = async () => fetchApi('/obtenerAlumnos');
export const obtenerAlumnoPorCedula = async (cedula) => fetchApi(`/obtenerAlumno/cedula/${cedula}`);
export const insertarAlumno = async (data) => fetchApi('/insertarAlumno', { method: 'POST', body: JSON.stringify(data) });
export const actualizarAlumno = async (data) => fetchApi('/actualizarAlumno', { method: 'POST', body: JSON.stringify(data) });
export const eliminarAlumno = async (cedula) => fetchApi(`/eliminarAlumno/${cedula}`, { method: 'DELETE' });

// CRUD para Ciclos
export const obtenerCiclos = async () => fetchApi('/obtenerCiclos');
export const obtenerCicloPorId = async (id) => fetchApi(`/obtenerCicloPorId/${id}`);
export const insertarCiclo = async (data) => fetchApi('/insertarCiclo', { method: 'POST', body: JSON.stringify(data) });
export const actualizarCiclo = async (data) => fetchApi('/actualizarCiclo', { method: 'POST', body: JSON.stringify(data) });
export const eliminarCiclo = async (anio, numero) => fetchApi(`/eliminarCiclo/${anio}/${numero}`, { method: 'DELETE' });

// CRUD para Grupos
export const obtenerGrupos = async () => fetchApi('/obtenerGrupos');
export const obtenerGrupoPorId = async (id) => fetchApi(`/obtenerGrupoPorId/${id}`);
export const insertarGrupo = async (data) => fetchApi('/insertarGrupo', { method: 'POST', body: JSON.stringify(data) });
export const actualizarGrupo = async (data) => fetchApi('/actualizarGrupo', { method: 'POST', body: JSON.stringify(data) });
export const eliminarGrupo = async (id) => fetchApi(`/eliminarGrupo/${id}`, { method: 'DELETE' });

// CRUD para Carrera-Curso
export const obtenerCarrerasCursos = async () => fetchApi('/obtenerCarrerasCursos');
export const insertarCarreraCurso = async (data) => fetchApi('/insertarCarreraCurso', { method: 'POST', body: JSON.stringify(data) });
export const actualizarCarreraCurso = async (data) => fetchApi('/actualizarCarreraCurso', { method: 'POST', body: JSON.stringify(data) });
export const eliminarCarreraCurso = async (id) => fetchApi(`/eliminarCarreraCurso/${id}`, { method: 'DELETE' });