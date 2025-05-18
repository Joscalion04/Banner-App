// src/components/AlumnoDashboard/components/CursosSection.js
import React from 'react';
import styles from '../../../styles/App.module.css';
import ResumenAcademico from './ResumenAcademico';
// A:
import { 
    desmatricularCurso,
    cargarMatriculas,
    cargarCursosDisponibles 
} from '../alumnoApi';

const CursosSection = ({ matriculas, setError, user, setMatriculas, setCursosConDetalle }) => {
    const handleDesmatricular = async (grupoId) => {
        try {
            await desmatricularCurso(grupoId, user.cedula);
            
            // Actualizar datos después de desmatricular
            const data = await cargarMatriculas(user.cedula);
            setMatriculas(data);
            
            const cursos = await cargarCursosDisponibles();
            setCursosConDetalle(cursos);
            
            setError(null);
        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <div className={styles.cursosSection}>
            <h2>Mis Cursos</h2>
            
            <ResumenAcademico matriculas={matriculas} />
            
            {matriculas.length > 0 ? (
                <table className={styles.historialTable}>
                    <thead>
                        <tr>
                            <th>Curso</th>
                            <th>Grupo</th>
                            <th>Horario</th>
                            <th>Nota</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        {matriculas.map(matricula => (
                            <tr key={matricula.matriculaId}>
                                <td>{matricula.grupoInfo?.nombreCurso || matricula.grupoInfo?.codigoCurso}</td>
                                <td>{matricula.grupoInfo?.numeroGrupo}</td>
                                <td>{matricula.grupoInfo?.horario}</td>
                                <td>{matricula.nota?.toFixed(1) || 'En progreso'}</td>
                                <td>
                                    {matricula.nota ? 
                                        (matricula.nota >= 70 ? 'Aprobado' : 'Reprobado') : 
                                        'En curso'}
                                </td>
                                <td>
                                    {!matricula.nota && (
                                        <button 
                                            onClick={() => handleDesmatricular(matricula.grupoId)}
                                            className={styles.dangerButton}
                                        >
                                            Desmatricular
                                        </button>
                                    )}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            ) : (
                <p>No tienes cursos registrados.</p>
            )}
        </div>
    );
};

export default CursosSection;