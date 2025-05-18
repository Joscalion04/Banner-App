// src/components/AlumnoDashboard/components/ResumenAcademico.js
import React from 'react';
import styles from '../../../styles/App.module.css';

const ResumenAcademico = ({ matriculas }) => {
    const calcularResumen = () => {
        const aprobados = matriculas.filter(m => m.nota >= 70).length;
        const enProgreso = matriculas.filter(m => m.nota === null || m.nota === undefined).length;
        const promedio = matriculas.length > 0 
            ? matriculas.reduce((sum, m) => sum + (m.nota || 0), 0) / matriculas.length
            : 0;

        return {
            promedio: promedio.toFixed(2),
            cursosAprobados: aprobados,
            cursosEnProgreso: enProgreso
        };
    };

    const resumen = calcularResumen();

    return (
        <div className={styles.resumenAcademico}>
            <h3>Resumen Académico</h3>
            <p>Promedio: {resumen.promedio}</p>
            <p>Cursos aprobados: {resumen.cursosAprobados}</p>
            <p>Cursos en progreso: {resumen.cursosEnProgreso}</p>
        </div>
    );
};

export default ResumenAcademico;