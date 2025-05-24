import styles from '../../../styles/App.module.css';

const Sidebar = ({ activeSection, activeCRUDSection, setActiveSection, setActiveCRUDSection }) => {
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
        </aside>
    );
};

export default Sidebar;