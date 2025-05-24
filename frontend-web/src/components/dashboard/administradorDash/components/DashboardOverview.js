import styles from '../../../styles/App.module.css';
import { FaChalkboardTeacher, FaUserGraduate, FaBook, FaCalendarAlt, FaUsers } from 'react-icons/fa';

const DashboardOverview = ({ teachers, students, courses, cycles, groups }) => {
    return (
        <div className={styles.dashboardOverview}>
            <div className={styles.header}>
                <h2>Panel de Administración</h2>
                <p className={styles.subtitle}>Bienvenido al sistema de administración académica</p>
            </div>
            
            <div className={styles.statsContainer}>
                {/* Tarjeta de Profesores */}
                <div className={`${styles.statCard} ${styles.teacherCard}`}>
                    <div className={styles.cardIcon}>
                        <FaChalkboardTeacher size={24} />
                    </div>
                    <div className={styles.cardContent}>
                        <h3>Profesores</h3>
                        <p className={styles.statNumber}>{teachers.length}</p>
                        <p className={styles.statLabel}>Registrados</p>
                    </div>
                </div>

                {/* Tarjeta de Alumnos */}
                <div className={`${styles.statCard} ${styles.studentCard}`}>
                    <div className={styles.cardIcon}>
                        <FaUserGraduate size={24} />
                    </div>
                    <div className={styles.cardContent}>
                        <h3>Alumnos</h3>
                        <p className={styles.statNumber}>{students.length}</p>
                        <p className={styles.statLabel}>Registrados</p>
                    </div>
                </div>

                {/* Tarjeta de Cursos */}
                <div className={`${styles.statCard} ${styles.courseCard}`}>
                    <div className={styles.cardIcon}>
                        <FaBook size={24} />
                    </div>
                    <div className={styles.cardContent}>
                        <h3>Cursos</h3>
                        <p className={styles.statNumber}>{courses.length}</p>
                        <p className={styles.statLabel}>Disponibles</p>
                    </div>
                </div>

                {/* Tarjeta de Ciclos */}
                <div className={`${styles.statCard} ${styles.cycleCard}`}>
                    <div className={styles.cardIcon}>
                        <FaCalendarAlt size={24} />
                    </div>
                    <div className={styles.cardContent}>
                        <h3>Ciclos</h3>
                        <p className={styles.statNumber}>{cycles.length}</p>
                        <p className={styles.statLabel}>Activos</p>
                    </div>
                </div>

                {/* Tarjeta de Grupos */}
                <div className={`${styles.statCard} ${styles.groupCard}`}>
                    <div className={styles.cardIcon}>
                        <FaUsers size={24} />
                    </div>
                    <div className={styles.cardContent}>
                        <h3>Grupos</h3>
                        <p className={styles.statNumber}>{groups.length}</p>
                        <p className={styles.statLabel}>Activos</p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default DashboardOverview;