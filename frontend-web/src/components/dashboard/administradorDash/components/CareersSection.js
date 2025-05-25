import { useState } from 'react';
import styles from '../../../styles/App.module.css';
import useCRUDOperations from '../hooks/useCRUDOperations';
import Swal from 'sweetalert2';

const CareersSection = ({ careers, onDelete }) => {
  const { openCreateModal, openEditModal } = useCRUDOperations('careers', {
    careers
  });

  const handleDelete = async (codigo) => {
    const result = await Swal.fire({
      title: '¿Eliminar carrera?',
      text: "Esta acción no se puede deshacer",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    });

    if (result.isConfirmed) {
      try {
        await onDelete(codigo);
        Swal.fire(
          'Eliminada!',
          'La carrera ha sido eliminada.',
          'success'
        );
      } catch (error) {
        Swal.fire(
          'Error!',
          error.message || 'No se pudo eliminar la carrera',
          'error'
        );
      }
    }
  };

  return (
    <div className={styles.crudSection}>
      <div className={styles.sectionHeader}>
        <h3>Gestión de Carreras</h3>
        <button 
          onClick={openCreateModal} 
          className={`${styles.btn} ${styles.btnAdd}`}
        >
          + Nueva Carrera
        </button>
      </div>
      
      <div className={styles.tableContainer}>
        <table className={`${styles.dataTable} ${styles.careersTable}`}>
          <thead>
            <tr>
              <th>Código</th>
              <th>Nombre</th>
              <th>Título</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {careers.map(career => (
              <tr key={career.codigoCarrera}>
                <td>{career.codigoCarrera}</td>
                <td>{career.nombre}</td>
                <td>{career.titulo}</td>
                <td>
                  <div className={styles.actionButtons}>
                    <button 
                      onClick={() => openEditModal(career)}
                      className={`${styles.btn} ${styles.btnEdit}`}
                    >
                      Editar
                    </button>
                    <button 
                      onClick={() => handleDelete(career.codigoCarrera)}
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

export default CareersSection;