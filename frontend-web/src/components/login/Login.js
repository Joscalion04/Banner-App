// src/components/Login.js

import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../styles/App.module.css';
import { realLoginAPI } from '../api/auth';


const Login = ({ onLogin }) => {
    const [cedula, setCedula] = useState('');
    const [clave, setClave] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            const data = await realLoginAPI(cedula, clave);
            onLogin(data);

            if (data.tipoUsuario === 'ALUMNO') {
                navigate('/alumno');
            } else if (data.tipoUsuario === 'PROFESOR') {
                navigate('/profesor');
            } else {
                throw new Error('Tipo de usuario desconocido');
            }
        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <div className={styles.loginContainer}>
            <form onSubmit={handleSubmit} className={styles.loginForm}>
                <h1>Ingreso Seguro</h1>
                {error && <p className={styles.error}>{error}</p>}
                <label>Cédula</label>
                <input
                    type="text"
                    placeholder="Ingrese su cédula"
                    value={cedula}
                    onChange={(e) => setCedula(e.target.value)}
                    required
                />
                <label>Clave</label>
                <input
                    type="password"
                    placeholder="Ingrese su clave"
                    value={clave}
                    onChange={(e) => setClave(e.target.value)}
                    required
                />
                <button type="submit">Iniciar Sesión</button>
            </form>
        </div>
    );
};

export default Login;
