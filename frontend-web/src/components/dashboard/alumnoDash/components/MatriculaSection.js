// src/components/AlumnoDashboard/components/MatriculaSection.js
import React from 'react';
import styles from '../../../styles/App.module.css';
import CursosDisponibles from './CursosDisponibles';
import { matricularCurso, cargarMatriculas, cargarCursosDisponibles } from '../alumnoApi';

const MatriculaSection = ({ 
    cursosConDetalle, 
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

    const handleCancel = () => {
        setSelectedGrupoId(null);
    };

    return (
        <div className={styles.matriculaSection}>
            <h2>Proceso de Matrícula</h2>
            
            <CursosDisponibles 
                cursosConDetalle={cursosConDetalle}
                selectedGrupoId={selectedGrupoId}
                setSelectedGrupoId={setSelectedGrupoId}
                onMatricular={handleMatricular}
                onCancel={handleCancel}
                loading={loading}
            />
        </div>
    );
};

export default MatriculaSection;