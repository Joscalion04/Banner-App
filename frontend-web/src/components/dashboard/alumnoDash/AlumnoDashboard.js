// src/components/AlumnoDashboard/AlumnoDashboard.js
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../../styles/App.module.css';
import Sidebar from './components/Sidebar';
import CursosSection from './components/CursosSection';
import MatriculaSection from './components/MatriculaSection';
import PerfilSection from './components/PerfilSection';
import { 
    cargarPerfilAlumno, 
    cargarMatriculas, 
    cargarCursosDisponibles 
} from './alumnoApi';

const AlumnoDashboard = ({ user }) => {
    const [activeTab, setActiveTab] = useState('cursos');
    const [matriculas, setMatriculas] = useState([]);
    const [perfil, setPerfil] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [cursosConDetalle, setCursosConDetalle] = useState([]);
    const [selectedGrupoId, setSelectedGrupoId] = useState(null);
    const navigate = useNavigate();

    // Cargar datos del alumno
    useEffect(() => {
        const loadProfile = async () => {
            setLoading(true);
            setError(null);
            try {
                const data = await cargarPerfilAlumno(user.cedula);
                setPerfil(data);
            } catch (err) {
                console.error('Error al cargar perfil:', err);
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };
        
        loadProfile();
    }, [user.cedula]);

    // Cargar datos según la pestaña activa
    useEffect(() => {
        const loadData = async () => {
            setLoading(true);
            setError(null);
            try {
                if (activeTab === 'cursos') {
                    const data = await cargarMatriculas(user.cedula);
                    setMatriculas(data);
                } else if (activeTab === 'matricula') {
                    const data = await cargarCursosDisponibles();
                    setCursosConDetalle(data);
                }
            } catch (err) {
                console.error(`Error al cargar datos (${activeTab}):`, err);
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };
        
        loadData();
    }, [activeTab, user.cedula]);

    return (
        <div className={styles.dashboardLayout}>
            <Sidebar 
                activeTab={activeTab}
                setActiveTab={setActiveTab}
                perfil={perfil}
                user={user}
            />
            
            <main className={styles.mainContent}>
                <header className={styles.header}>
                    <h1>Bienvenido, {perfil ? perfil.nombre : user.cedula}</h1>
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
                        {activeTab === 'cursos' && (
                            <CursosSection 
                                matriculas={matriculas}
                                setError={setError}
                                user={user}
                                setMatriculas={setMatriculas}
                                setCursosConDetalle={setCursosConDetalle}
                            />
                        )}
                        
                        {activeTab === 'matricula' && (
                            <MatriculaSection 
                                cursosConDetalle={cursosConDetalle}
                                selectedGrupoId={selectedGrupoId}
                                setSelectedGrupoId={setSelectedGrupoId}
                                setError={setError}
                                user={user}
                                setMatriculas={setMatriculas}
                                setCursosConDetalle={setCursosConDetalle}
                                loading={loading}
                            />
                        )}
                        
                        {activeTab === 'perfil' && (
                            <PerfilSection perfil={perfil} />
                        )}
                    </section>
                )}
            </main>
        </div>
    );
};

export default AlumnoDashboard;