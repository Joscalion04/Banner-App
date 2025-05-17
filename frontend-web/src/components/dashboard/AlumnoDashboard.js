import styles from '../styles/App.module.css';

const AlumnoDashboard = ({ user }) => {
    return (
        <div className={styles.dashboardLayout}>
            <aside className={styles.sidebar}>
                <h2>Alumno</h2>
                <ul>
                    <li>Mis Cursos</li>
                    <li>Notas</li>
                    <li>Foros</li>
                    <li>Perfil</li>
                </ul>
            </aside>
            <main className={styles.mainContent}>
                <header className={styles.header}>
                    Bienvenido, {user.cedula}
                </header>
                <section>
                    <h3>Panel de Alumno</h3>
                    <p>Contenido del dashboard tipo Moodle (alumno).</p>
                </section>
            </main>
        </div>
    );
};

export default AlumnoDashboard;
