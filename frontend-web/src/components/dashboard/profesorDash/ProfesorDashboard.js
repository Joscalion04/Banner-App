import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../../styles/App.module.css';
import {
    obtenerProfesorPorCedula,
    obtenerGruposProfesor,
    obtenerAlumnosMatriculados,
    registrarNotasAlumnos,
    actualizarPerfilProfesor
} from './profesorApi';

const ProfesorDashboard = ({ user, onLogout }) => {
    const navigate = useNavigate();
    const [activeTab, setActiveTab] = useState('grupos');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    
    const [profesor, setProfesor] = useState({
        cedula: user.cedula,
        nombre: '',
        telefono: '',
        email: ''
    });
    
    const [grupos, setGrupos] = useState([]);
    const [selectedGrupoId, setSelectedGrupoId] = useState(null);
    const [alumnosMatriculados, setAlumnosMatriculados] = useState([]);
    const [notasTemporales, setNotasTemporales] = useState({});
    const [editMode, setEditMode] = useState(false);
    const [editData, setEditData] = useState({
        nombre: '',
        telefono: '',
        email: ''
    });

    const handleLogout = () => {
        onLogout();
        navigate('/login');
    };

    // Cargar datos del profesor
    useEffect(() => {
        const cargarPerfilProfesor = async () => {
            setLoading(true);
            setError(null);
            try {
                const data = await obtenerProfesorPorCedula(user.cedula);
                setProfesor({
                    cedula: data.cedula,
                    nombre: data.nombre,
                    telefono: data.telefono || '',
                    email: data.email
                });
                setEditData({
                    nombre: data.nombre,
                    telefono: data.telefono || '',
                    email: data.email
                });
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };
        
        cargarPerfilProfesor();
    }, [user.cedula]);

    // Cargar grupos del profesor
    useEffect(() => {
        const cargarGruposProfesor = async () => {
            setLoading(true);
            setError(null);
            try {
                const gruposProfesor = await obtenerGruposProfesor(user.cedula);
                setGrupos(gruposProfesor);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };
        
        cargarGruposProfesor();
    }, [user.cedula]);

    // Cargar alumnos matriculados cuando se selecciona un grupo
    useEffect(() => {
        if (selectedGrupoId) {
            const cargarAlumnosMatriculados = async () => {
                setLoading(true);
                setError(null);
                try {
                    const alumnos = await obtenerAlumnosMatriculados(selectedGrupoId);
                    setAlumnosMatriculados(alumnos);
                } catch (err) {
                    setError(err.message);
                } finally {
                    setLoading(false);
                }
            };
            
            cargarAlumnosMatriculados();
        }
    }, [selectedGrupoId]);

    const handleNotaChange = (cedulaAlumno, nota) => {
        setNotasTemporales(prev => ({
            ...prev,
            [cedulaAlumno]: nota
        }));
    };

    const registrarNotas = async () => {
        setLoading(true);
        setError(null);
        
        try {
            const { resultados, errores } = await registrarNotasAlumnos({
                grupoId: selectedGrupoId,
                notasTemporales
            });

            // Mostrar resultados
            if (errores.length > 0) {
                const mensajeError = errores.map(e => 
                    `Alumno ${e.cedulaAlumno}: ${e.message}`
                ).join('\n');
                
                setError(`Algunas notas no se registraron:\n${mensajeError}`);
            } else {
                // Mostrar mensaje de éxito
                setError('Todas las notas se registraron correctamente');
                setTimeout(() => setError(null), 3000);
            }

            // Actualizar la lista de alumnos después de registrar las notas
            const alumnos = await obtenerAlumnosMatriculados(selectedGrupoId);
            setAlumnosMatriculados(alumnos);
            
            // Limpiar solo las notas que se registraron exitosamente
            setNotasTemporales(prev => {
                const nuevasNotas = { ...prev };
                resultados.forEach(r => {
                    delete nuevasNotas[r.cedulaAlumno];
                });
                return nuevasNotas;
            });

        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const actualizarPerfil = async () => {
        setLoading(true);
        setError(null);
        try {
            await actualizarPerfilProfesor({
                cedula: profesor.cedula,
                nombre: editData.nombre,
                telefono: editData.telefono,
                email: editData.email
            });
            
            setProfesor(prev => ({
                ...prev,
                nombre: editData.nombre,
                telefono: editData.telefono,
                email: editData.email
            }));
            setEditMode(false);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const renderContent = () => {
        switch (activeTab) {
            case 'grupos':
                return (
                    <div className={styles.sectionContainer}>
                        <h2>Mis Grupos</h2>
                        
                        <div className={styles.gruposGrid}>
                            {grupos.map(grupo => (
                                <div 
                                    key={grupo.grupoId}
                                    className={`${styles.grupoCard} ${selectedGrupoId === grupo.grupoId ? styles.selected : ''}`}
                                    onClick={() => setSelectedGrupoId(grupo.grupoId)}
                                >
                                    <h3>Curso: {grupo.codigoCurso}</h3>
                                    <p>Grupo: {grupo.numeroGrupo}</p>
                                    <p>Horario: {grupo.horario}</p>
                                </div>
                            ))}
                        </div>
                    </div>
                );
                
            case 'calificaciones':
                return (
                    <div className={styles.sectionContainer}>
                        <h2>Registro de Calificaciones</h2>
                        
                        <div className={styles.gruposSelector}>
                            <label>Seleccione un grupo:</label>
                            <select 
                                value={selectedGrupoId || ''}
                                onChange={(e) => setSelectedGrupoId(parseInt(e.target.value))}
                            >
                                <option value="">-- Seleccione --</option>
                                {grupos.map(grupo => (
                                    <option key={grupo.grupoId} value={grupo.grupoId}>
                                        {grupo.codigoCurso} - Grupo {grupo.numeroGrupo}
                                    </option>
                                ))}
                            </select>
                        </div>
                        
                        {selectedGrupoId && (
                            <div className={styles.calificacionesTable}>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Nombre</th>
                                            <th>Cédula</th>
                                            <th>Nota Actual</th>
                                            <th>Nueva Nota</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {alumnosMatriculados.map(alumno => (
                                            <tr key={alumno.cedulaAlumno}>
                                                <td>{alumno.nombre}</td>
                                                <td>{alumno.cedulaAlumno}</td>
                                                <td>{alumno.nota || 'Sin registrar'}</td>
                                                <td>
                                                    <input
                                                        type="number"
                                                        min="0"
                                                        max="100"
                                                        value={notasTemporales[alumno.cedulaAlumno] || ''}
                                                        onChange={(e) => handleNotaChange(alumno.cedulaAlumno, e.target.value)}
                                                    />
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                                
                                <button 
                                    onClick={registrarNotas}
                                    className={styles.primaryButton}
                                    disabled={Object.keys(notasTemporales).length === 0}
                                >
                                    Guardar Notas
                                </button>
                            </div>
                        )}
                    </div>
                );
                
            case 'perfil':
                return (
                    <div className={styles.sectionContainer}>
                        <h2>Mi Perfil</h2>
                        
                        {editMode ? (
                            <form onSubmit={(e) => {
                                e.preventDefault();
                                actualizarPerfil();
                            }} className={styles.profileForm}>
                                <div className={styles.formGroup}>
                                    <label>Nombre:</label>
                                    <input
                                        type="text"
                                        value={editData.nombre}
                                        onChange={(e) => setEditData({...editData, nombre: e.target.value})}
                                        required
                                    />
                                </div>
                                
                                <div className={styles.formGroup}>
                                    <label>Teléfono:</label>
                                    <input
                                        type="tel"
                                        value={editData.telefono}
                                        onChange={(e) => setEditData({...editData, telefono: e.target.value})}
                                    />
                                </div>
                                
                                <div className={styles.formGroup}>
                                    <label>Email:</label>
                                    <input
                                        type="email"
                                        value={editData.email}
                                        onChange={(e) => setEditData({...editData, email: e.target.value})}
                                        required
                                    />
                                </div>
                                
                                <div className={styles.formActions}>
                                    <button type="submit" className={styles.primaryButton}>
                                        Guardar Cambios
                                    </button>
                                    <button 
                                        type="button" 
                                        onClick={() => setEditMode(false)}
                                        className={styles.secondaryButton}
                                    >
                                        Cancelar
                                    </button>
                                </div>
                            </form>
                        ) : (
                            <div className={styles.profileInfo}>
                                <p><strong>Nombre:</strong> {profesor.nombre}</p>
                                <p><strong>Cédula:</strong> {profesor.cedula}</p>
                                <p><strong>Teléfono:</strong> {profesor.telefono || 'No registrado'}</p>
                                <p><strong>Email:</strong> {profesor.email}</p>
                                
                                <button 
                                    onClick={() => setEditMode(true)}
                                    className={styles.primaryButton}
                                >
                                    Editar Perfil
                                </button>
                            </div>
                        )}
                    </div>
                );
                
            default:
                return <p>Seleccione una opción del menú</p>;
        }
    };

    return (
        <div className={styles.dashboardLayout}>
            <aside className={styles.sidebar}>
                <div className={styles.userProfile}>
                    <div className={styles.profileHeader}>
                        <h2>Profesor</h2>
                        <button 
                            onClick={handleLogout}
                            className={styles.logoutButton}
                        >
                            Cerrar Sesión
                        </button>
                    </div>
                    {profesor.nombre && (
                        <div className={styles.userInfo}>
                            <p>{profesor.nombre}</p>
                            <p>Cédula: {profesor.cedula}</p>
                            <p>Email: {profesor.email}</p>
                        </div>
                    )}
                </div>
                
                <ul className={styles.navMenu}>
                    <li 
                        className={activeTab === 'grupos' ? styles.active : ''}
                        onClick={() => setActiveTab('grupos')}
                    >
                        Mis Grupos
                    </li>
                    <li 
                        className={activeTab === 'calificaciones' ? styles.active : ''}
                        onClick={() => setActiveTab('calificaciones')}
                    >
                        Calificaciones
                    </li>
                    <li 
                        className={activeTab === 'perfil' ? styles.active : ''}
                        onClick={() => setActiveTab('perfil')}
                    >
                        Perfil
                    </li>
                </ul>
            </aside>
            
            <main className={styles.mainContent}>
                <header className={styles.header}>
                    <h1>Bienvenido, {profesor.nombre || user.cedula}</h1>
                </header>
                
                {error && (
                    <div className={styles.errorAlert}>
                        Error: {error}
                        <button onClick={() => setError(null)}>Cerrar</button>
                    </div>
                )}
                
                {loading ? (
                    <div className={styles.loadingIndicator}>
                        Cargando...
                    </div>
                ) : (
                    <section className={styles.dashboardContent}>
                        {renderContent()}
                    </section>
                )}
            </main>
        </div>
    );
};

export default ProfesorDashboard;