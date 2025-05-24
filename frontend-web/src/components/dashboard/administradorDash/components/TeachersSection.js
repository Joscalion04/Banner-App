import styles from '../../../styles/App.module.css';
import useCRUDOperations from '../hooks/useCRUDOperations';

const TeachersSection = ({ teachers, onDelete }) => {
    const { openCreateModal, openEditModal } = useCRUDOperations('teachers', {
        careers: [],
        courses: [],
        teachers,
        academicCycles: []
    });

    return (
        <div className={styles.crudSection}>
            <h3>Gestión de Profesores</h3>
            <button onClick={openCreateModal} className={`${styles.btn} ${styles.btnAdd}`}>
                Agregar Nuevo Profesor
            </button>
            
            <div className={styles.tableContainer}>
                <table className={`${styles.dataTable} ${styles.teachersTable}`}>
                    <thead>
                        <tr>
                            <th>Cédula</th>
                            <th>Nombre</th>
                            <th>Email</th>
                            <th>Teléfono</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        {teachers.map(teacher => (
                            <tr key={teacher.cedula}>
                                <td>{teacher.cedula}</td>
                                <td>{teacher.nombre}</td>
                                <td>{teacher.email}</td>
                                <td>{teacher.telefono || 'N/A'}</td>
                                <td>
                                    <div className={styles.actionButtons}>
                                        <button 
                                            onClick={() => openEditModal(teacher)} 
                                            className={`${styles.btn} ${styles.btnEdit}`}
                                        >
                                            Editar
                                        </button>
                                        <button 
                                            onClick={() => onDelete(teacher.cedula)}
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

export default TeachersSection;