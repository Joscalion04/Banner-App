import { useState } from 'react';
import styles from '../../../styles/App.module.css';
import useCRUDOperations from '../hooks/useCRUDOperations';
import Swal from 'sweetalert2';

const UsersSection = ({ users, onDelete }) => {
  const { openCreateModal, openEditModal } = useCRUDOperations('users');
  const [showPassword, setShowPassword] = useState(false);

  const handleDelete = async (cedula) => {
    const result = await Swal.fire({
      title: '¿Eliminar usuario?',
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
        await onDelete(cedula);
        Swal.fire('Eliminado!', 'El usuario ha sido eliminado.', 'success');
      } catch (error) {
        Swal.fire('Error!', error.message || 'No se pudo eliminar el usuario', 'error');
      }
    }
  };

  return (
    <div className={styles.crudSection}>
      <div className={styles.sectionHeader}>
        <h3>Gestión de Usuarios</h3>
        <button 
          onClick={openCreateModal} 
          className={`${styles.btn} ${styles.btnAdd}`}
        >
          + Nuevo Usuario
        </button>
      </div>

      <div className={styles.tableContainer}>
        <table className={`${styles.dataTable} ${styles.usersTable}`}>
          <thead>
            <tr>
              <th>Cédula</th>
              <th>Contraseña</th>
              <th>Tipo de Usuario</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {users.map(user => (
              <tr key={user.cedula}>
                <td>{user.cedula}</td>
                <td>
                  {showPassword ? user.clave : '••••••••'}
                  <button 
                    onClick={() => setShowPassword(!showPassword)} 
                    className={styles.showPasswordBtn}
                  >
                    {showPassword ? 'Ocultar' : 'Mostrar'}
                  </button>
                </td>
                <td>{user.tipoUsuario}</td>
                <td>
                  <div className={styles.actionButtons}>
                    <button 
                      onClick={() => openEditModal(user)}
                      className={`${styles.btn} ${styles.btnEdit}`}
                    >
                      Editar
                    </button>
                    <button 
                      onClick={() => handleDelete(user.cedula)}
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

export default UsersSection;