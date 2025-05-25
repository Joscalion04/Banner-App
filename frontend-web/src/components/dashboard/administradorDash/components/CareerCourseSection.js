import { useState } from 'react';
import styles from '../../../styles/App.module.css';
import useCRUDOperations from '../hooks/useCRUDOperations';
import Swal from 'sweetalert2';

const CareerCourseSection = ({ careerCourses, careers, courses, onDelete }) => {
  const { openCreateModal, openEditModal } = useCRUDOperations('careerCourses', {
    careers,
    courses
  });

  const handleDelete = async (id) => {
    const result = await Swal.fire({
      title: '¿Eliminar relación?',
      text: "Esta acción no se puede deshacer",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    });

    if (result.isConfirmed) {
      try {
        await onDelete(id);
        Swal.fire('Eliminada!', 'La relación ha sido eliminada.', 'success');
      } catch (error) {
        Swal.fire(
          'Error!',
          error.message || 'No se pudo eliminar la relación',
          'error'
        );
      }
    }
  };

  return (
    <div className={styles.crudSection}>
      <div className={styles.sectionHeader}>
        <h3>Gestión de Relaciones Carrera-Curso</h3>
        <button 
          onClick={openCreateModal} 
          className={`${styles.btn} ${styles.btnAdd}`}
        >
          + Nueva Relación
        </button>
      </div>
      
      <div className={styles.tableContainer}>
        <table className={`${styles.dataTable} ${styles.careerCoursesTable}`}>
          <thead>
            <tr>
              <th>ID</th>
              <th>Carrera</th>
              <th>Curso</th>
              <th>Año</th>
              <th>Ciclo</th>
              <th>Orden</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {careerCourses.map(cc => {
              const career = careers.find(c => c.codigoCarrera === cc.codigoCarrera);
              const course = courses.find(c => c.codigoCurso === cc.codigoCurso);
              
              return (
                <tr key={cc.carreraCursoId}>
                  <td>{cc.carreraCursoId}</td>
                  <td>{career?.nombre || cc.codigoCarrera}</td>
                  <td>{course?.nombre || cc.codigoCurso}</td>
                  <td>{cc.anio}</td>
                  <td>{cc.ciclo}</td>
                  <td>{cc.orden}</td>
                  <td>
                    <div className={styles.actionButtons}>
                      <button 
                        onClick={() => openEditModal(cc)}
                        className={`${styles.btn} ${styles.btnEdit}`}
                      >
                        Editar
                      </button>
                      <button 
                        onClick={() => handleDelete(cc.carreraCursoId)}
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

export default CareerCourseSection;