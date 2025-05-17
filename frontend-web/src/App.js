import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { useState } from 'react';
import Login from './components/login/Login';
import AlumnoDashboard from './components/dashboard/AlumnoDashboard';
import ProfesorDashboard from './components/dashboard/ProfesorDashboard';

function App() {
    const [user, setUser] = useState(null);

    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to="/login" replace />} />
                <Route path="/login" element={<Login onLogin={setUser} />} />
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
            </Routes>
        </Router>
    );
}

export default App;
