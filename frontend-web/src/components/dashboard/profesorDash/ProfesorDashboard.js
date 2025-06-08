import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../../styles/App.module.css';

const ProfesorDashboard = ({ user, onLogout }) => {
    const navigate = useNavigate();
    const [activeTab, setActiveTab] = useState('grupos');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    
    // Estado del profesor
    const [profesor, setProfesor] = useState({
        cedula: user.cedula,
        nombre: '',
        telefono: '',
        email: ''
    });
    
    // Estado para los grupos
    const [grupos, setGrupos] = useState([]);
    const [selectedGrupoId, setSelectedGrupoId] = useState(null);
    
    // Estado para alumnos matriculados y notas
    const [alumnosMatriculados, setAlumnosMatriculados] = useState([]);
    const [notasTemporales, setNotasTemporales] = useState({});
    
    // Estado para edición de perfil
    const [editMode, setEditMode] = useState(false);
    const [editData, setEditData] = useState({
        nombre: '',
        telefono: '',
        email: ''
    });

    // Función para manejar logout
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
                const response = await fetch(`/api/obtenerProfesor/id/${user.cedula}`);
                if (!response.ok) {
                    throw new Error('Error al cargar perfil del profesor');
                }
                const data = await response.json();
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
                const response = await fetch('/api/obtenerGrupos');
                if (!response.ok) {
                    throw new Error('Error al cargar grupos');
                }
                const data = await response.json();
                const gruposProfesor = data.filter(grupo => grupo.cedulaProfesor === user.cedula);
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
                    const response = await fetch(`/api/consultarHistorialAcademico?grupoId=${selectedGrupoId}`);
                    if (!response.ok) {
                        throw new Error('Error al cargar alumnos matriculados');
                    }
                    const data = await response.json();
                    
                    // Obtener información detallada de cada alumno
                    const alumnosConInfo = await Promise.all(
                        data.map(async matricula => {
                            const alumnoResponse = await fetch(`/api/obtenerAlumno/cedula/${matricula.cedulaAlumno}`);
                            if (!alumnoResponse.ok) {
                                return {
                                    ...matricula,
                                    nombre: 'Nombre no disponible'
                                };
                            }
                            const alumnoData = await alumnoResponse.json();
                            return {
                                ...matricula,
                                nombre: alumnoData.nombre
                            };
                        })
                    );
                    
                    setAlumnosMatriculados(alumnosConInfo);
                } catch (err) {
                    setError(err.message);
                } finally {
                    setLoading(false);
                }
            };
            
            cargarAlumnosMatriculados();
        }
    }, [selectedGrupoId]);

    // Manejar cambio de notas temporales
    const handleNotaChange = (cedulaAlumno, nota) => {
        setNotasTemporales(prev => ({
            ...prev,
            [cedulaAlumno]: nota
        }));
    };

    // Registrar notas en el backend
    const registrarNotas = async () => {
        setLoading(true);
        setError(null);
        try {
            const notasParaRegistrar = Object.entries(notasTemporales)
                .filter(([_, nota]) => nota !== undefined && nota !== '');
            
            for (const [cedulaAlumno, nota] of notasParaRegistrar) {
                const response = await fetch('/api/registrarNota', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        grupoId: selectedGrupoId,
                        cedulaAlumno: cedulaAlumno,
                        nota: parseInt(nota)
                    })
                });
                
                if (!response.ok) {
                    const errorData = await response.json();
                    throw new Error(errorData.message || 'Error al registrar nota');
                }
            }
            
            // Actualizar la lista de alumnos después de registrar las notas
            const response = await fetch(`/api/consultarHistorialAcademico?grupoId=${selectedGrupoId}`);
            const data = await response.json();
            setAlumnosMatriculados(data);
            setNotasTemporales({});
            
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    // Actualizar perfil del profesor
    const actualizarPerfil = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await fetch('/api/actualizarProfesor', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    cedula: profesor.cedula,
                    nombre: editData.nombre,
                    telefono: editData.telefono,
                    email: editData.email
                })
            });
            
            if (!response.ok) {
                throw new Error('Error al actualizar perfil');
            }
            
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

    // Renderizar contenido según la pestaña activa
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