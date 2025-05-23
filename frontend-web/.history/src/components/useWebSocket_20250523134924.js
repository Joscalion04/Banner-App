import { useEffect, useRef } from 'react';

export function useWebSocket(onEvento) {
  const socketRef = useRef(null);

  useEffect(() => {
    const socket = new WebSocket('ws://localhost:8080/ws');
    socketRef.current = socket;

    socket.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data);
        const tipo = data.tipo || 'desconocido';
        const evento = data.evento || 'desconocido';
        const id = data.id || '';
        onEvento(tipo, evento, id);
      } catch (error) {
        console.error('Error al procesar mensaje WebSocket:', error);
      }
    };

    return () => {
      socket.close(1000);
    };
  }, [onEvento]);

  return socketRef;
}

/*
Ejemplo de uso:

import React, { useEffect } from 'react';
import { useWebSocket } from './hooks/useWebSocket';

function App() {
  const cargarCursos = async () => {
    console.log("Cargando cursos...");
  };

  useWebSocket((tipo, evento, id) => {
    if (
      tipo === 'curso' &&
      (evento === 'insertar' || evento === 'actualizar' || evento === 'eliminar')
    ) {
      cargarCursos();
    }
  });

  return (
    <div>
      <h1>Lista de Cursos</h1>
      //{ Mostrar cursos aquí }
    </div>
  );
}

export default App;

*/