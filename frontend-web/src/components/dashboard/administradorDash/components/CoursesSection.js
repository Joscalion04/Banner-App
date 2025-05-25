import styles from '../../../styles/App.module.css';
import useCRUDOperations from '../hooks/useCRUDOperations';

const CoursesSection = ({ courses, onDelete }) => {
    const { openCreateModal, openEditModal } = useCRUDOperations('courses');

    return (
        <div className={styles.crudSection}>
            <h3>Gestión de Cursos</h3>
            <button onClick={openCreateModal} className={`${styles.btn} ${styles.btnAdd}`}>
                Agregar Nuevo Curso
            </button>
            
            <div className={styles.tableContainer}>
                <table className={`${styles.dataTable} ${styles.coursesTable}`}>
                    <thead>
                        <tr>
                            <th>Código</th>
                            <th>Nombre</th>
                            <th>Créditos</th>
                            <th>Horas Semanales</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        {courses.map(course => (
                            <tr key={course.codigoCurso}>
                                <td>{course.codigoCurso}</td>
                                <td>{course.nombre}</td>
                                <td>{course.creditos}</td>
                                <td>{course.horasSemanales}</td>
                                <td>
                                    <div className={styles.actionButtons}>
                                        <button 
                                            onClick={() => openEditModal(course)} 
                                            className={`${styles.btn} ${styles.btnEdit}`}
                                        >
                                            Editar
                                        </button>
                                        <button 
                                            onClick={() => onDelete(course.codigoCurso)}
                                            className={`${styles.btn} ${styles.btnDelete}`}
                                        >
                                            Eliminar
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default CoursesSection;