import styles from '../../../styles/App.module.css';
import useCRUDOperations from '../hooks/useCRUDOperations';

const StudentsSection = ({ students, careers, onDelete }) => {
    const { openCreateModal, openEditModal } = useCRUDOperations('students', {
        careers,
        courses: [],
        teachers: [],
        academicCycles: []
    });

    return (
        <div className={styles.crudSection}>
            <h3>Gestión de Alumnos</h3>
            <button onClick={openCreateModal} className={`${styles.btn} ${styles.btnAdd}`}>
                Agregar Nuevo Alumno
            </button>
            
            <div className={styles.tableContainer}>
                <table className={`${styles.dataTable} ${styles.studentsTable}`}>
                    <thead>
                        <tr>
                            <th>Cédula</th>
                            <th>Nombre</th>
                            <th>Email</th>
                            <th>Carrera</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        {students.map(student => {
                            const career = careers.find(c => c.codigoCarrera === student.codigoCarrera);
                            return (
                                <tr key={student.cedula}>
                                    <td>{student.cedula}</td>
                                    <td>{student.nombre}</td>
                                    <td>{student.email}</td>
                                    <td>{career ? career.nombre : student.codigoCarrera}</td>
                                    <td>
                                        <div className={styles.actionButtons}>
                                            <button 
                                                onClick={() => openEditModal(student)} 
                                                className={`${styles.btn} ${styles.btnEdit}`}
                                            >
                                                Editar
                                            </button>
                                            <button 
                                                onClick={() => onDelete(student.cedula)}
                                                className={`${styles.btn} ${styles.btnDelete}`}
                                            >
                                                Eliminar
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            );
                        })}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default StudentsSection;