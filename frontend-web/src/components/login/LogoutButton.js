// src/components/common/LogoutButton.js
import { useNavigate } from 'react-router-dom';
import styles from '../styles/App.module.css';

const LogoutButton = ({ onLogout }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    // Ejecutar función de logout proporcionada
    if (onLogout) onLogout();
    
    // Redirigir
    navigate('/login');
  };

  return (
    <button 
      onClick={handleClick}
      className={styles.logoutButton}
    >
      Cerrar Sesión
    </button>
  );
};

export default LogoutButton;