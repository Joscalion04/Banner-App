const API_BASE_URL = 'http://localhost:8080/api';

export const fetchApi = async (endpoint, options = {}) => {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        headers: {
            'Content-Type': 'application/json',
            ...options.headers
        },
        ...options
    });
    const textResponse = await response.text();

    try {
        const data = textResponse ? JSON.parse(textResponse) : null;
        
        if (!response.ok) {
            const errorMsg = data?.message || textResponse || `Error ${response.status}`;
            throw new Error(errorMsg);
        }
        
        return data || { success: true, message: textResponse };
    } catch (e) {
        if (response.ok) {
            return { success: true, message: textResponse };
        }
        throw new Error(textResponse || `Error ${response.status}`);
    }
};

// Funciones específicas para el profesor
export const obtenerProfesorPorCedula = async (cedula) => {
    return fetchApi(`/obtenerProfesor/id/${cedula}`);
};

export const obtenerGruposProfesor = async (cedula) => {
    const grupos = await fetchApi('/obtenerGrupos');
    return grupos.filter(grupo => grupo.cedulaProfesor === cedula);
};

export const obtenerAlumnosMatriculados = async (grupoId) => {
    const matriculas = await fetchApi(`/obtenerMatriculasPorGrupo/${grupoId}`);
    
    const alumnosConInfo = await Promise.all(
        matriculas.map(async matricula => {
            try {
                const alumnoData = await fetchApi(`/obtenerAlumno/cedula/${matricula.cedulaAlumno}`);
                return {
                    ...matricula,
                    nombre: alumnoData.nombre
                };
            } catch {
                return {
                    ...matricula,
                    nombre: 'Nombre no disponible'
                };
            }
        })
    );
    
    return alumnosConInfo;
};

export const registrarNotasAlumnos = async (notasData) => {
    const { grupoId, notasTemporales } = notasData;
    
    // Validar que haya notas para registrar
    const notasParaRegistrar = Object.entries(notasTemporales)
        .filter(([_, nota]) => nota !== undefined && nota !== '');
    
    if (notasParaRegistrar.length === 0) {
        throw new Error('No hay notas para registrar');
    }

    // Validar que las notas sean números válidos entre 0 y 100
    const notasInvalidas = notasParaRegistrar.filter(([_, nota]) => {
        const notaNum = parseInt(nota);
        return isNaN(notaNum) || notaNum < 0 || notaNum > 100;
    });

    if (notasInvalidas.length > 0) {
        throw new Error('Las notas deben ser números entre 0 y 100');
    }

    // Registrar las notas
    const resultados = [];
    const errores = [];

    for (const [cedulaAlumno, nota] of notasParaRegistrar) {
        try {
            const resultado = await fetchApi('/registrarNota', {
                method: 'POST',
                body: JSON.stringify({
                    grupoId,
                    cedulaAlumno,
                    nota: parseInt(nota)
                })
            });
            resultados.push({
                cedulaAlumno,
                success: true,
                message: resultado.message || 'Nota registrada correctamente'
            });
        } catch (error) {
            errores.push({
                cedulaAlumno,
                success: false,
                message: error.message
            });
        }
    }

    return {
        resultados,
        errores,
        totalRegistros: notasParaRegistrar.length,
        exitosos: resultados.length,
        fallidos: errores.length
    };
};

export const actualizarPerfilProfesor = async (profesorData) => {
    return fetchApi('/actualizarProfesor', {
        method: 'POST',
        body: JSON.stringify(profesorData)
    });
};