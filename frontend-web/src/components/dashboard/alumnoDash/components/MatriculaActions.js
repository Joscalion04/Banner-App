// src/components/AlumnoDashboard/components/MatriculaActions.js
import React from 'react';
import styles from '../../../styles/App.module.css';

const MatriculaActions = ({ 
    handleMatricular,
    handleCancel,
    loading
}) => {
    return (
        <div className={styles.inlineButtonGroup}>
            <button 
                onClick={handleMatricular}
                className={styles.primaryButton}
                disabled={loading}
            >
                {loading ? 'Procesando...' : 'Matricular'}
            </button>
            <button 
                onClick={handleCancel}
                className={styles.secondaryButton}
            >
                Cancelar
            </button>
        </div>
    );
};

export default MatriculaActions;