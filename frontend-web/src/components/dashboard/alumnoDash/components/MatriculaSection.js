// src/components/AlumnoDashboard/components/MatriculaSection.js
import React from 'react';
import styles from '../../../styles/App.module.css';
import CursosDisponibles from './CursosDisponibles';
import MatriculaActions from './MatriculaActions';

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
    return (
        <div className={styles.matriculaSection}>
            <h2>Proceso de Matrícula</h2>
            
            <div className={styles.matriculaContainer}>
                <CursosDisponibles 
                    cursosConDetalle={cursosConDetalle}
                    selectedGrupoId={selectedGrupoId}
                    setSelectedGrupoId={setSelectedGrupoId}
                />
                
                <MatriculaActions 
                    selectedGrupoId={selectedGrupoId}
                    setSelectedGrupoId={setSelectedGrupoId}
                    setError={setError}
                    user={user}
                    setMatriculas={setMatriculas}
                    setCursosConDetalle={setCursosConDetalle}
                    loading={loading}
                />
            </div>
        </div>
    );
};

export default MatriculaSection;