export const realLoginAPI = async (cedula, clave) => {
    const response = await fetch('http://localhost:8080/api/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ cedula, clave }),
    });

    if (!response.ok) {
        throw new Error('Credenciales inválidas');
    }

    const data = await response.json();
    return data;
};
