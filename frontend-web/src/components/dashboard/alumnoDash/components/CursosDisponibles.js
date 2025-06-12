// src/components/AlumnoDashboard/components/CursosDisponibles.js
import React from 'react';
import styles from '../../../styles/App.module.css';
import MatriculaActions from './MatriculaActions';

const CursosDisponibles = ({ 
    cursosConDetalle, 
    selectedGrupoId, 
    setSelectedGrupoId,
    onMatricular,
    onCancel,
    loading
}) => {
    return (
        <div className={styles.cursosDisponibles}>
            <h3>Cursos Disponibles</h3>
            {cursosConDetalle.length > 0 ? (
                <div className={styles.cursosGrid}>
                    {cursosConDetalle.map(grupo => (
                        <div 
                            key={grupo.grupoId} 
                            className={`${styles.cursoCard} ${selectedGrupoId === grupo.grupoId ? styles.selected : ''}`}
                        >
                            <div 
                                className={styles.cursoContent}
                                onClick={() => setSelectedGrupoId(grupo.grupoId)}
                            >
                                <h4>{grupo.nombreCurso || grupo.codigoCurso}</h4>
                                <p><strong>Grupo:</strong> {grupo.numeroGrupo}</p>
                                <p><strong>Horario:</strong> {grupo.horario}</p>
                                <p><strong>Créditos:</strong> {grupo.creditos}</p>
                                <p><strong>Código:</strong> {grupo.codigoCurso}</p>
                            </div>
                            
                            {selectedGrupoId === grupo.grupoId && (
                                <MatriculaActions 
                                    handleMatricular={onMatricular}
                                    handleCancel={onCancel}
                                    loading={loading}
                                />
                            )}
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