// src/components/Dashboard.js

import styles from '../styles/App.module.css';

const Dashboard = ({ user, onLogout }) => {
    return (
        <div className={styles.container}>
            <h2>Bienvenido, {user.name}</h2>
            <button onClick={onLogout}>Cerrar sesión</button>
        </div>
    );
};

export default Dashboard;
