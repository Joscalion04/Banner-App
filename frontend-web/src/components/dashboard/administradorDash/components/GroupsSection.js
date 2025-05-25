import styles from '../../../styles/App.module.css';
import useCRUDOperations from '../hooks/useCRUDOperations';

const GroupsSection = ({ groups, courses, teachers, cycles, onDelete }) => {
    const { openCreateModal, openEditModal } = useCRUDOperations('groups', {
        careers: [],
        courses,
        teachers,
        academicCycles: cycles
    });

    return (
        <div className={styles.crudSection}>
            <h3>Gestión de Grupos</h3>
            <button onClick={openCreateModal} className={`${styles.btn} ${styles.btnAdd}`}>
                Agregar Nuevo Grupo
            </button>
            
            <div className={styles.tableContainer}>
                <table className={`${styles.dataTable} ${styles.groupsTable}`}>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Curso</th>
                            <th>Profesor</th>
                            <th>Ciclo</th>
                            <th>Horario</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        {groups.map(group => {
                            const course = courses.find(c => c.codigoCurso === group.codigoCurso) || {};
                            const teacher = teachers.find(t => t.cedula === group.cedulaProfesor) || {};
                            const cycle = cycles.find(c => 
                                c.anio === group.anio && c.numero === group.cicloId
                            ) || {};
                            
                            return (
                                <tr key={group.grupoId}>
                                    <td>{group.grupoId}</td>
                                    <td>{course.nombre || group.codigoCurso}</td>
                                    <td>{teacher.nombre || group.cedulaProfesor}</td>
                                    <td>Ciclo: {group.cicloId}</td>
                                    <td>{group.horario}</td>
                                    <td>
                                        <div className={styles.actionButtons}>
                                            <button 
                                                onClick={() => openEditModal(group)} 
                                                className={`${styles.btn} ${styles.btnEdit}`}
                                            >
                                                Editar
                                            </button>
                                            <button 
                                                onClick={() => onDelete(group.grupoId)}
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

export default GroupsSection;