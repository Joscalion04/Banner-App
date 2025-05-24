import styles from '../../../styles/App.module.css';

const DashboardOverview = ({ teachers, students, courses, cycles, groups }) => {
    return (
        <div className={styles.dashboardOverview}>
            <h3>Panel de Administración</h3>
            <p>Bienvenido al sistema de administración académica.</p>
            
            <div className={styles.statsContainer}>
                <div className={styles.statCard}>
                    <h4>Profesores</h4>
                    <p>{teachers.length} registrados</p>
                </div>
                <div className={styles.statCard}>
                    <h4>Alumnos</h4>
                    <p>{students.length} registrados</p>
                </div>
                <div className={styles.statCard}>
                    <h4>Cursos</h4>
                    <p>{courses.length} registrados</p>
                </div>
                <div className={styles.statCard}>
                    <h4>Ciclos</h4>
                    <p>{cycles.length} activos</p>
                </div>
                <div className={styles.statCard}>
                    <h4>Grupos</h4>
                    <p>{groups.length} activos</p>
                </div>
            </div>
        </div>
    );
};

export default DashboardOverview;