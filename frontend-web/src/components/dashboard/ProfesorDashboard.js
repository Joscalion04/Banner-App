import styles from '../styles/App.module.css';

const ProfesorDashboard = ({ user }) => {
    return (
        <div className={styles.dashboardLayout}>
            <aside className={styles.sidebar}>
                <h2>Profesor</h2>
                <ul>
                    <li>Mis Grupos</li>
                    <li>Calificaciones</li>
                    <li>Mensajes</li>
                    <li>Perfil</li>
                </ul>
            </aside>
            <main className={styles.mainContent}>
                <header className={styles.header}>
                    Bienvenido, {user.cedula}
                </header>
                <section>
                    <h3>Panel de Profesor</h3>
                    <p>Contenido del dashboard tipo Moodle (profesor).</p>
                </section>
            </main>
        </div>
    );
};

export default ProfesorDashboard;
