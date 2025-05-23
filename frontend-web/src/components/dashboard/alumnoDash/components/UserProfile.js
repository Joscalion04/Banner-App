// src/components/AlumnoDashboard/components/UserProfile.js
import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../../../styles/App.module.css';

const UserProfile = ({ perfil, user, onLogout }) => {
    const navigate = useNavigate();

    const handleLogout = () => {
        onLogout(); // Usamos la prop onLogout en lugar de setUser directamente
        navigate('/login');
    };

    return (
        <div className={styles.userProfile}>
            <div className={styles.profileHeader}>
                <h2>Alumno</h2>
                <button 
                    onClick={handleLogout}
                    className={styles.logoutButton}
                >
                    Cerrar Sesión
                </button>
            </div>
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