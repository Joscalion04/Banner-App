// src/components/AlumnoDashboard/api/alumnoApi.js
export const fetchApi = async (endpoint) => {
    const response = await fetch(`http://localhost:8080/api${endpoint}`);
    
    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || `Error ${response.status}`);
    }
    
    return response.json();
};

export const cargarPerfilAlumno = async (cedula) => {
    return fetchApi(`/obtenerAlumno/cedula/${cedula}`);
};

export const cargarMatriculas = async (cedula) => {
    const data = await fetchApi(`/consultarHistorialAcademico/${cedula}`);
    
    if (data && data.length > 0) {
        const gruposDetalle = await Promise.all(
            data.map(matricula => 
                fetchApi(`/obtenerGrupoPorId/${matricula.grupoId}`)
            )
        );
        
        return data.map((matricula, index) => ({
            ...matricula,
            grupoInfo: gruposDetalle[index]
        }));
    }
    
    return data || [];
};

export const cargarCursosDisponibles = async () => {
    const grupos = await fetchApi('/obtenerGrupos');
    const cursosUnicos = [...new Set(grupos.map(g => g.codigoCurso))];
    const detallesCursos = await Promise.all(
        cursosUnicos.map(codigo => 
            fetchApi(`/obtenerCurso/code/${codigo}`)
        )
    );
    
    return grupos.map(grupo => {
        const cursoInfo = detallesCursos.find(c => c.codigo_curso === grupo.codigoCurso);
        return {
            ...grupo,
            nombreCurso: cursoInfo?.nombre || grupo.codigoCurso,
            creditos: cursoInfo?.creditos
        };
    });
};

export const matricularCurso = async (grupoId, cedulaAlumno) => {
    const response = await fetch('http://localhost:8080/api/registrarMatricula', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            grupoId: grupoId,
            cedulaAlumno: cedulaAlumno
        })
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Error al matricular');
    }

    return response.json();
};

export const desmatricularCurso = async (grupoId, cedulaAlumno) => {
    const response = await fetch('http://localhost:8080/api/eliminarMatricula', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            grupoId: grupoId,
            cedulaAlumno: cedulaAlumno
        })
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Error al desmatricular');
    }

    return response.json();
};