// src/components/AlumnoDashboard/components/CursosDisponibles.js
import React from 'react';
import styles from '../../../styles/App.module.css';

const CursosDisponibles = ({ cursosConDetalle, selectedGrupoId, setSelectedGrupoId }) => {
    return (
        <div className={styles.cursosDisponibles}>
            <h3>Cursos Disponibles</h3>
            {cursosConDetalle.length > 0 ? (
                <div className={styles.cursosGrid}>
                    {cursosConDetalle.map(grupo => (
                        <div 
                            key={grupo.grupoId} 
                            className={`${styles.cursoCard} ${selectedGrupoId === grupo.grupoId ? styles.selected : ''}`}
                            onClick={() => setSelectedGrupoId(grupo.grupoId)}
                        >
                            <h4>{grupo.nombreCurso || grupo.codigoCurso}</h4>
                            <p><strong>Grupo:</strong> {grupo.numeroGrupo}</p>
                            <p><strong>Horario:</strong> {grupo.horario}</p>
                            <p><strong>Créditos:</strong> {grupo.creditos}</p>
                            <p><strong>Código:</strong> {grupo.codigoCurso}</p>
                        </div>
                    ))}
                </div>
            ) : (
                <p>No hay cursos disponibles para matricular.</p>
            )}
        </div>
    );
};

export default CursosDisponibles;