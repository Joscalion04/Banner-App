import styles from '../../../styles/App.module.css';
import useCRUDOperations from '../hooks/useCRUDOperations';

const CyclesSection = ({ cycles, onDelete }) => {
    const { openCreateModal, openEditModal } = useCRUDOperations('academicCycles');

    return (
        <div className={styles.crudSection}>
            <h3>Gestión de Ciclos Académicos</h3>
            <button onClick={openCreateModal} className={`${styles.btn} ${styles.btnAdd}`}>
                Agregar Nuevo Ciclo
            </button>
            
            <div className={styles.tableContainer}>
                <table className={`${styles.dataTable} ${styles.cyclesTable}`}>
                    <thead>
                        <tr>
                            <th>Año</th>
                            <th>Número</th>
                            <th>Fecha Inicio</th>
                            <th>Fecha Fin</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        {cycles.map(cycle => (
                            <tr key={`${cycle.anio}-${cycle.numero}`}>
                                <td>{cycle.anio}</td>
                                <td>{cycle.numero}</td>
                                <td>{new Date(cycle.fechaInicio).toLocaleDateString()}</td>
                                <td>{new Date(cycle.fechaFin).toLocaleDateString()}</td>
                                <td>
                                    <div className={styles.actionButtons}>
                                        <button 
                                            onClick={() => openEditModal(cycle)} 
                                            className={`${styles.btn} ${styles.btnEdit}`}
                                        >
                                            Editar
                                        </button>
                                        <button 
                                            onClick={() => onDelete(cycle.anio, cycle.numero)}
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

export default CyclesSection;