import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../styles/App.module.css';
import { realLoginAPI } from './auth';

const Login = ({ onLogin }) => {
    const [cedula, setCedula] = useState('');
    const [clave, setClave] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        
        try {
            const userData = await realLoginAPI(cedula, clave);
            
            // Asegúrate que tu API devuelva estos campos
            if (!userData || !userData.tipoUsuario) {
                throw new Error('Datos de usuario inválidos');
            }
            
            // Guarda los datos del usuario en el estado de App
            onLogin({
                cedula: userData.cedula,
                nombre: userData.nombre,
                tipoUsuario: userData.tipoUsuario
                // Agrega otros campos necesarios
            });

            // Redirección basada en el tipo de usuario
            if (userData.tipoUsuario === 'ALUMNO') {
                navigate('/alumno');
            } else if (userData.tipoUsuario === 'PROFESOR') {
                navigate('/profesor');
            } else if (userData.tipoUsuario === 'ADMINISTRADOR') {
                navigate('/administrador');
            } else {
                throw new Error('Tipo de usuario no reconocido');
            }
            
        } catch (err) {
            setError(err.message || 'Error al iniciar sesión');
            console.error('Error en login:', err);
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