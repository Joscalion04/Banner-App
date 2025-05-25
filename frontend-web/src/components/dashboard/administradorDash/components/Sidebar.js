import styles from '../../../styles/App.module.css';
import LogoutButton from '../../../login/LogoutButton'; // Ajusta la ruta según tu estructura
import { useNavigate } from 'react-router-dom';

const Sidebar = ({ activeSection, activeCRUDSection, setActiveSection, setActiveCRUDSection ,onLogout}) => {
    const navigate = useNavigate();
    return (
        <aside className={styles.sidebar}>
            <h2>Administrador</h2>
            <ul>
                <li 
                    className={activeSection === 'dashboard' ? styles.active : ''}
                    onClick={() => { setActiveSection('dashboard'); setActiveCRUDSection(null); }}
                >
                    Dashboard
                </li>
                <li 
                    className={activeCRUDSection === 'teachers' ? styles.active : ''}
                    onClick={() => { setActiveCRUDSection('teachers'); }}
                >
                    Profesores
                </li>
                <li 
                    className={activeCRUDSection === 'students' ? styles.active : ''}
                    onClick={() => { setActiveCRUDSection('students'); }}
                >
                    Alumnos
                </li>
                <li 
                    className={activeCRUDSection === 'courses' ? styles.active : ''}
                    onClick={() => { setActiveCRUDSection('courses'); }}
                >
                    Cursos
                </li>
                <li 
                    className={activeCRUDSection === 'academicCycles' ? styles.active : ''}
                    onClick={() => { setActiveCRUDSection('academicCycles'); }}
                >
                    Ciclos Académicos
                </li>
                <li 
                    className={activeCRUDSection === 'groups' ? styles.active : ''}
                    onClick={() => { setActiveCRUDSection('groups'); }}
                >
                    Grupos
                </li>
            </ul>
             <div className={styles.sidebarFooter}>
                <LogoutButton 
                    onLogout={() => {

                        onLogout(); // Usamos la prop onLogout en lugar de setUser directamente
                        navigate('/login');
                    }} 
                />
            </div>
        </aside>
    );
};

export default Sidebar;