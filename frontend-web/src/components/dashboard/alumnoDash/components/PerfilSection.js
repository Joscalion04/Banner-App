// src/components/AlumnoDashboard/components/PerfilSection.js
import React from 'react';
import styles from '../../../styles/App.module.css';

const PerfilSection = ({ perfil }) => {
    return (
        <div className={styles.perfilSection}>
            <h2>Mi Perfil</h2>
            {perfil ? (
                <div className={styles.perfilInfo}>
                    <div className={styles.perfilField}>
                        <label>Nombre completo:</label>
                        <p>{perfil.nombre}</p>
                    </div>
                    <div className={styles.perfilField}>
                        <label>Cédula:</label>
                        <p>{perfil.cedula}</p>
                    </div>
                    <div className={styles.perfilField}>
                        <label>Teléfono:</label>
                        <p>{perfil.telefono || 'No registrado'}</p>
                    </div>
                    <div className={styles.perfilField}>
                        <label>Email:</label>
                        <p>{perfil.email || 'No registrado'}</p>
                    </div>
                    <div className={styles.perfilField}>
                        <label>Carrera:</label>
                        <p>{perfil.codigoCarrera || 'No asignada'}</p>
                    </div>
                </div>
            ) : (
                <p>No se pudo cargar la información del perfil.</p>
            )}
        </div>
    );
};

export default PerfilSection;