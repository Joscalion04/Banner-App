// src/components/AlumnoDashboard/components/MatriculaActions.js
import React from 'react';
import styles from '../../../styles/App.module.css';
import { matricularCurso, cargarMatriculas, cargarCursosDisponibles } from '../alumnoApi';

const MatriculaActions = ({ 
    selectedGrupoId, 
    setSelectedGrupoId,
    setError,
    user,
    setMatriculas,
    setCursosConDetalle,
    loading
}) => {
    const handleMatricular = async () => {
        if (!selectedGrupoId) {
            setError('Debes seleccionar un grupo');
            return;
        }

        try {
            await matricularCurso(selectedGrupoId, user.cedula);
            
            // Actualizar datos después de matricular
            const matriculas = await cargarMatriculas(user.cedula);
            setMatriculas(matriculas);
            
            const cursos = await cargarCursosDisponibles();
            setCursosConDetalle(cursos);
            
            setSelectedGrupoId(null);
            setError(null);
        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <div className={styles.matriculaActions}>
            {selectedGrupoId && (
                <>
                    <button 
                        onClick={handleMatricular}
                        className={styles.primaryButton}
                        disabled={loading}
                    >
                        {loading ? 'Procesando...' : 'Matricular en este curso'}
                    </button>
                    <button 
                        onClick={() => setSelectedGrupoId(null)}
                        className={styles.secondaryButton}
                    >
                        Cancelar selección
                    </button>
                </>
            )}
        </div>
    );
};

export default MatriculaActions;