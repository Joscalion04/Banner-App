// src/components/AlumnoDashboard/components/UserProfile.js
import React from 'react';
import styles from '../../../styles/App.module.css';

const UserProfile = ({ perfil, user }) => {
    return (
        <div className={styles.userProfile}>
            <h2>Alumno</h2>
            {perfil && (
                <div className={styles.userInfo}>
                    <p>{perfil.nombre}</p>
                    <p>Cédula: {perfil.cedula}</p>
                    <p>Carrera: {perfil.codigoCarrera}</p>
                </div>
            )}
        </div>
    );
};

export default UserProfile;