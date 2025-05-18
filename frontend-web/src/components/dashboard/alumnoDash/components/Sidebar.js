// src/components/AlumnoDashboard/components/Sidebar.js
import React from 'react';
import styles from '../../../styles/App.module.css';
import UserProfile from './UserProfile';

const Sidebar = ({ activeTab, setActiveTab, perfil, user }) => {
    return (
        <aside className={styles.sidebar}>
            <UserProfile perfil={perfil} user={user} />
            
            <ul className={styles.navMenu}>
                <li 
                    className={activeTab === 'cursos' ? styles.active : ''}
                    onClick={() => setActiveTab('cursos')}
                >
                    Mis Cursos
                </li>
                <li 
                    className={activeTab === 'matricula' ? styles.active : ''}
                    onClick={() => setActiveTab('matricula')}
                >
                    Matrícula
                </li>
                <li 
                    className={activeTab === 'perfil' ? styles.active : ''}
                    onClick={() => setActiveTab('perfil')}
                >
                    Perfil
                </li>
            </ul>
        </aside>
    );
};

export default Sidebar;