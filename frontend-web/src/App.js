import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import Login from './components/login/Login';
import AlumnoDashboard from './components/dashboard/alumnoDash/AlumnoDashboard';
import ProfesorDashboard from './components/dashboard/profesorDash/ProfesorDashboard';

function App() {
    const [user, setUser] = useState(() => {
        // Recupera el usuario de localStorage si existe
        const savedUser = localStorage.getItem('user');
        return savedUser ? JSON.parse(savedUser) : null;
    });

    // Guarda el usuario en localStorage cuando cambia
    useEffect(() => {
        if (user) {
            localStorage.setItem('user', JSON.stringify(user));
        } else {
            localStorage.removeItem('user');
        }
    }, [user]);

    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to="/login" replace />} />
                <Route 
                    path="/login" 
                    element={user ? (
                        <Navigate to={user.tipoUsuario === 'ALUMNO' ? '/alumno' : '/profesor'} replace />
                    ) : (
                        <Login onLogin={setUser} />
                    )} 
                />
                <Route 
                    path="/alumno" 
                    element={user && user.tipoUsuario === 'ALUMNO' ? (
                        <AlumnoDashboard user={user} />
                    ) : (
                        <Navigate to="/login" replace />
                    )}
                />
                <Route 
                    path="/profesor" 
                    element={user && user.tipoUsuario === 'PROFESOR' ? (
                        <ProfesorDashboard user={user} />
                    ) : (
                        <Navigate to="/login" replace />
                    )}
                />
                {/* Ruta para manejar páginas no encontradas */}
                <Route path="*" element={<Navigate to="/login" replace />} />
            </Routes>
        </Router>
    );
}

export default App;