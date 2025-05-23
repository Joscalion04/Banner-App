import { useState, useEffect } from 'react';
import styles from '../../styles/App.module.css';
import * as adminApi from './adminApi';
import Swal from 'sweetalert2';

const AdminDashboard = ({ user }) => {
    const [activeSection, setActiveSection] = useState('dashboard');
    const [activeCRUDSection, setActiveCRUDSection] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    // Estados para los datos
    const [teachers, setTeachers] = useState([]);
    const [students, setStudents] = useState([]);
    const [courses, setCourses] = useState([]);
    const [academicCycles, setAcademicCycles] = useState([]);
    const [groups, setGroups] = useState([]);
    const [careers, setCareers] = useState([]);
    const [careerCourses, setCareerCourses] = useState([]);

    // Cargar datos iniciales
    useEffect(() => {
        const loadInitialData = async () => {
            setLoading(true);
            setError(null);
            try {
                const [
                    careersData, 
                    teachersData, 
                    studentsData, 
                    coursesData, 
                    cyclesData, 
                    groupsData,
                    careerCoursesData
                ] = await Promise.all([
                    adminApi.obtenerCarreras(),
                    adminApi.obtenerProfesores(),
                    adminApi.obtenerAlumnos(),
                    adminApi.obtenerCursos(),
                    adminApi.obtenerCiclos(),
                    adminApi.obtenerGrupos(),
                    adminApi.obtenerCarrerasCursos()
                ]);
                
                setCareers(careersData);
                setTeachers(teachersData);
                setStudents(studentsData);
                setCourses(coursesData);
                setAcademicCycles(cyclesData);
                setGroups(groupsData);
                setCareerCourses(careerCoursesData);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };
        
        loadInitialData();
    }, []);

    // Funciones CRUD para Profesores
    const handleDeleteTeacher = async (cedula) => {
        if (!window.confirm('¿Está seguro de eliminar este profesor?')) return;
        
        try {
            setLoading(true);
            await adminApi.eliminarProfesor(cedula);
            setTeachers(teachers.filter(t => t.cedula !== cedula));
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    // Funciones CRUD para Alumnos
    const handleDeleteStudent = async (cedula) => {
        if (!window.confirm('¿Está seguro de eliminar este alumno?')) return;
        
        try {
            setLoading(true);
            await adminApi.eliminarAlumno(cedula);
            setStudents(students.filter(s => s.cedula !== cedula));
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    // Funciones CRUD para Cursos
    const handleDeleteCourse = async (codigo) => {
        if (!window.confirm('¿Está seguro de eliminar este curso?')) return;
        
        try {
            setLoading(true);
            await adminApi.eliminarCurso(codigo);
            setCourses(courses.filter(c => c.codigoCurso !== codigo));
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    // Funciones CRUD para Ciclos
    const handleDeleteCycle = async (anio, numero) => {
        if (!window.confirm('¿Está seguro de eliminar este ciclo?')) return;
        
        try {
            setLoading(true);
            await adminApi.eliminarCiclo(anio, numero);
            setAcademicCycles(academicCycles.filter(c => !(c.anio === anio && c.numero === numero)));
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    // Funciones CRUD para Grupos
    const handleDeleteGroup = async (grupoId) => {
        if (!window.confirm('¿Está seguro de eliminar este grupo?')) return;
        
        try {
            setLoading(true);
            await adminApi.eliminarGrupo(grupoId);
            setGroups(groups.filter(g => g.grupoId !== grupoId));
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    // Función para abrir el modal de creación con SweetAlert
    const openCreateModal = () => {
        const entityName = getEntityName();
        const form = renderSweetAlertForm();
        
        Swal.fire({
            title: `Nuevo ${entityName}`,
            html: form,
            showCancelButton: true,
            confirmButtonText: 'Guardar',
            cancelButtonText: 'Cancelar',
            showLoaderOnConfirm: true,
            preConfirm: () => {
                return handleSweetAlertSubmit(false);
            },
            allowOutsideClick: () => !Swal.isLoading()
        });
    };

    // Función para abrir el modal de edición con SweetAlert
    const openEditModal = (data) => {
        const entityName = getEntityName();
        const form = renderSweetAlertForm(data);
        
        Swal.fire({
            title: `Editar ${entityName}`,
            html: form,
            showCancelButton: true,
            confirmButtonText: 'Actualizar',
            cancelButtonText: 'Cancelar',
            showLoaderOnConfirm: true,
            preConfirm: () => {
                return handleSweetAlertSubmit(true, data);
            },
            allowOutsideClick: () => !Swal.isLoading()
        });
    };

    // Renderizar el formulario para SweetAlert
    const renderSweetAlertForm = (data = {}) => {
        const formData = data || {};
        let formHTML = '';
        
        switch(activeCRUDSection) {
            case 'teachers':
                formHTML = `
                    <div class="sweet-form-group">
                        <label>Cédula:</label>
                        <input type="text" id="swal-cedula" class="swal2-input" value="${formData.cedula || ''}" placeholder="Cédula" required>
                    </div>
                    <div class="sweet-form-group">
                        <label>Nombre:</label>
                        <input type="text" id="swal-nombre" class="swal2-input" value="${formData.nombre || ''}" placeholder="Nombre" required>
                    </div>
                    <div class="sweet-form-group">
                        <label>Email:</label>
                        <input type="email" id="swal-email" class="swal2-input" value="${formData.email || ''}" placeholder="Email" required>
                    </div>
                    <div class="sweet-form-group">
                        <label>Teléfono:</label>
                        <input type="text" id="swal-telefono" class="swal2-input" value="${formData.telefono || ''}" placeholder="Teléfono">
                    </div>
                `;
                break;

            case 'students':
                const careerOptions = careers.map(c => 
                    `<option value="${c.codigoCarrera}" ${formData.codigoCarrera === c.codigoCarrera ? 'selected' : ''}>${c.nombre}</option>`
                ).join('');
                
                formHTML = `
                    <div class="sweet-form-group">
                        <label>Cédula:</label>
                        <input type="text" id="swal-cedula" class="swal2-input" value="${formData.cedula || ''}" placeholder="Cédula" required>
                    </div>
                    <div class="sweet-form-group">
                        <label>Nombre:</label>
                        <input type="text" id="swal-nombre" class="swal2-input" value="${formData.nombre || ''}" placeholder="Nombre" required>
                    </div>
                    <div class="sweet-form-group">
                        <label>Email:</label>
                        <input type="email" id="swal-email" class="swal2-input" value="${formData.email || ''}" placeholder="Email" required>
                    </div>
                    <div class="sweet-form-group">
                        <label>Fecha Nacimiento:</label>
                        <input type="date" id="swal-fechaNacimiento" class="swal2-input" value="${formData.fechaNacimiento || ''}" required>
                    </div>
                    <div class="sweet-form-group">
                        <label>Carrera:</label>
                        <select id="swal-codigoCarrera" class="swal2-input" required>
                            <option value="">Seleccione una carrera</option>
                            ${careerOptions}
                        </select>
                    </div>
                `;
                break;

            case 'courses':
                formHTML = `
                    <div class="sweet-form-group">
                        <label>Código:</label>
                        <input type="text" id="swal-codigoCurso" class="swal2-input" value="${formData.codigoCurso || ''}" placeholder="Código" required>
                    </div>
                    <div class="sweet-form-group">
                        <label>Nombre:</label>
                        <input type="text" id="swal-nombre" class="swal2-input" value="${formData.nombre || ''}" placeholder="Nombre" required>
                    </div>
                    <div class="sweet-form-group">
                        <label>Créditos:</label>
                        <input type="number" id="swal-creditos" class="swal2-input" value="${formData.creditos || ''}" placeholder="Créditos" required>
                    </div>
                    <div class="sweet-form-group">
                        <label>Horas Semanales:</label>
                        <input type="number" id="swal-horasSemanales" class="swal2-input" value="${formData.horasSemanales || ''}" placeholder="Horas Semanales" required>
                    </div>
                `;
                break;

            case 'academicCycles':
                const numeroSelected = formData.numero || '';
                formHTML = `
                    <div class="sweet-form-group">
                        <label>Año:</label>
                        <input type="number" id="swal-anio" class="swal2-input" value="${formData.anio || ''}" placeholder="Año" required>
                    </div>
                    <div class="sweet-form-group">
                        <label>Número:</label>
                        <select id="swal-numero" class="swal2-input" required>
                            <option value="">Seleccione</option>
                            <option value="1" ${numeroSelected === '1' ? 'selected' : ''}>1</option>
                            <option value="2" ${numeroSelected === '2' ? 'selected' : ''}>2</option>
                            <option value="3" ${numeroSelected === '3' ? 'selected' : ''}>3</option>
                        </select>
                    </div>
                    <div class="sweet-form-group">
                        <label>Fecha Inicio:</label>
                        <input type="date" id="swal-fechaInicio" class="swal2-input" value="${formData.fechaInicio ? formData.fechaInicio.split('T')[0] : ''}" required>
                    </div>
                    <div class="sweet-form-group">
                        <label>Fecha Fin:</label>
                        <input type="date" id="swal-fechaFin" class="swal2-input" value="${formData.fechaFin ? formData.fechaFin.split('T')[0] : ''}" required>
                    </div>
                `;
                break;

            case 'groups':
                const courseOptions = courses.map(c => 
                    `<option value="${c.codigoCurso}" ${formData.codigoCurso === c.codigoCurso ? 'selected' : ''}>${c.nombre}</option>`
                ).join('');
                
                const teacherOptions = teachers.map(t => 
                    `<option value="${t.cedula}" ${formData.cedulaProfesor === t.cedula ? 'selected' : ''}>${t.nombre}</option>`
                ).join('');
                
                const cycleOptions = academicCycles.map(c => 
                    `<option value="${c.anio}" ${formData.anio === c.anio ? 'selected' : ''}>${c.anio}-${c.numero}</option>`
                ).join('');
                
                formHTML = `
                    <div class="sweet-form-group">
                        <label>Curso:</label>
                        <select id="swal-codigoCurso" class="swal2-input" required>
                            <option value="">Seleccione un curso</option>
                            ${courseOptions}
                        </select>
                    </div>
                    <div class="sweet-form-group">
                        <label>Profesor:</label>
                        <select id="swal-cedulaProfesor" class="swal2-input" required>
                            <option value="">Seleccione un profesor</option>
                            ${teacherOptions}
                        </select>
                    </div>
                    <div class="sweet-form-group">
                        <label>Ciclo:</label>
                        <select id="swal-anio" class="swal2-input" required>
                            <option value="">Seleccione un ciclo</option>
                            ${cycleOptions}
                        </select>
                    </div>
                    <div class="sweet-form-group">
                        <label>Horario:</label>
                        <input type="text" id="swal-horario" class="swal2-input" value="${formData.horario || ''}" placeholder="Horario" required>
                    </div>
                `;
                break;

            default:
                break;
        }
        
        return formHTML;
    };

    // Manejar el submit del formulario de SweetAlert
    const handleSweetAlertSubmit = async (isEditing, originalData = {}) => {
        try {
            setLoading(true);
            let formData = {};
            
            switch(activeCRUDSection) {
                case 'teachers':
                    formData = {
                        cedula: document.getElementById('swal-cedula').value,
                        nombre: document.getElementById('swal-nombre').value,
                        email: document.getElementById('swal-email').value,
                        telefono: document.getElementById('swal-telefono').value
                    };
                    break;
                    
                case 'students':
                    formData = {
                        cedula: document.getElementById('swal-cedula').value,
                        nombre: document.getElementById('swal-nombre').value,
                        email: document.getElementById('swal-email').value,
                        fechaNacimiento: document.getElementById('swal-fechaNacimiento').value,
                        codigoCarrera: document.getElementById('swal-codigoCarrera').value
                    };
                    break;
                    
                case 'courses':
                    formData = {
                        codigoCurso: document.getElementById('swal-codigoCurso').value,
                        nombre: document.getElementById('swal-nombre').value,
                        creditos: parseInt(document.getElementById('swal-creditos').value),
                        horasSemanales: parseInt(document.getElementById('swal-horasSemanales').value)
                    };
                    break;
                    
                case 'academicCycles':
                    formData = {
                        anio: parseInt(document.getElementById('swal-anio').value),
                        numero: document.getElementById('swal-numero').value,
                        fechaInicio: document.getElementById('swal-fechaInicio').value,
                        fechaFin: document.getElementById('swal-fechaFin').value
                    };
                    break;
                    
                case 'groups':
                    const selectedCycle = academicCycles.find(c => 
                        c.anio === parseInt(document.getElementById('swal-anio').value)
                    );
                    
                    formData = {
                        codigoCurso: document.getElementById('swal-codigoCurso').value,
                        cedulaProfesor: document.getElementById('swal-cedulaProfesor').value,
                        anio: selectedCycle?.anio,
                        numeroCiclo: selectedCycle?.numero,
                        horario: document.getElementById('swal-horario').value,
                        grupoId: isEditing ? originalData.grupoId : undefined
                    };
                    break;
                    
                default:
                    break;
            }
            
            let result;
            
            switch(activeCRUDSection) {
                case 'teachers':
                    if (isEditing) {
                        await adminApi.actualizarProfesor(formData);
                        setTeachers(teachers.map(t => t.cedula === formData.cedula ? formData : t));
                    } else {
                        result = await adminApi.insertarProfesor(formData);
                        setTeachers([...teachers, result]);
                    }
                    break;
                case 'students':
                    if (isEditing) {
                        await adminApi.actualizarAlumno(formData);
                        setStudents(students.map(s => s.cedula === formData.cedula ? formData : s));
                    } else {
                        result = await adminApi.insertarAlumno(formData);
                        setStudents([...students, result]);
                    }
                    break;
                case 'courses':
                    if (isEditing) {
                        await adminApi.actualizarCurso(formData);
                        setCourses(courses.map(c => c.codigoCurso === formData.codigoCurso ? formData : c));
                    } else {
                        result = await adminApi.insertarCurso(formData);
                        setCourses([...courses, result]);
                    }
                    break;
                case 'academicCycles':
                    if (isEditing) {
                        await adminApi.actualizarCiclo(formData);
                        setAcademicCycles(academicCycles.map(c => 
                            (c.anio === formData.anio && c.numero === formData.numero) ? formData : c
                        ));
                    } else {
                        result = await adminApi.insertarCiclo(formData);
                        setAcademicCycles([...academicCycles, result]);
                    }
                    break;
                case 'groups':
                    if (isEditing) {
                        await adminApi.actualizarGrupo(formData);
                        setGroups(groups.map(g => g.grupoId === formData.grupoId ? formData : g));
                    } else {
                        result = await adminApi.insertarGrupo(formData);
                        setGroups([...groups, result]);
                    }
                    break;
                default:
                    break;
            }
            
            return true;
        } catch (err) {
            Swal.showValidationMessage(`Error: ${err.message}`);
            return false;
        } finally {
            setLoading(false);
        }
    };

    // Obtener el nombre de la entidad actual
    const getEntityName = () => {
        switch(activeCRUDSection) {
            case 'teachers': return 'Profesor';
            case 'students': return 'Alumno';
            case 'courses': return 'Curso';
            case 'academicCycles': return 'Ciclo';
            case 'groups': return 'Grupo';
            default: return '';
        }
    };

    // Renderizar la sección CRUD
    const renderCRUDSection = () => {
        if (loading) return <div className={styles.loading}>Cargando...</div>;
        if (error) return <div className={styles.error}>Error: {error}</div>;

        switch(activeCRUDSection) {
            case 'teachers':
                return (
                    <div className={styles.crudSection}>
                        <h3>Gestión de Profesores</h3>
                        <button onClick={openCreateModal} className={`${styles.btn} ${styles.btnAdd}`}>
                            Agregar Nuevo Profesor
                        </button>
                        
                        <div className={styles.tableContainer}>
                            <table className={`${styles.dataTable} ${styles.teachersTable}`}>
                                <thead>
                                    <tr>
                                        <th>Cédula</th>
                                        <th>Nombre</th>
                                        <th>Email</th>
                                        <th>Teléfono</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {teachers.map(teacher => (
                                        <tr key={teacher.cedula}>
                                            <td>{teacher.cedula}</td>
                                            <td>{teacher.nombre}</td>
                                            <td>{teacher.email}</td>
                                            <td>{teacher.telefono || 'N/A'}</td>
                                            <td>
                                                <div className={styles.actionButtons}>
                                                    <button 
                                                        onClick={() => openEditModal(teacher)} 
                                                        className={`${styles.btn} ${styles.btnEdit}`}
                                                    >
                                                        Editar
                                                    </button>
                                                    <button 
                                                        onClick={() => handleDeleteTeacher(teacher.cedula)}
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

            case 'students':
                return (
                    <div className={styles.crudSection}>
                        <h3>Gestión de Alumnos</h3>
                        <button onClick={openCreateModal} className={`${styles.btn} ${styles.btnAdd}`}>
                            Agregar Nuevo Alumno
                        </button>
                        
                        <div className={styles.tableContainer}>
                            <table className={`${styles.dataTable} ${styles.studentsTable}`}>
                                <thead>
                                    <tr>
                                        <th>Cédula</th>
                                        <th>Nombre</th>
                                        <th>Email</th>
                                        <th>Carrera</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {students.map(student => {
                                        const career = careers.find(c => c.codigoCarrera === student.codigoCarrera);
                                        return (
                                            <tr key={student.cedula}>
                                                <td>{student.cedula}</td>
                                                <td>{student.nombre}</td>
                                                <td>{student.email}</td>
                                                <td>{career ? career.nombre : student.codigoCarrera}</td>
                                                <td>
                                                    <div className={styles.actionButtons}>
                                                        <button 
                                                            onClick={() => openEditModal(student)} 
                                                            className={`${styles.btn} ${styles.btnEdit}`}
                                                        >
                                                            Editar
                                                        </button>
                                                        <button 
                                                            onClick={() => handleDeleteStudent(student.cedula)}
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

            case 'courses':
                return (
                    <div className={styles.crudSection}>
                        <h3>Gestión de Cursos</h3>
                        <button onClick={openCreateModal} className={`${styles.btn} ${styles.btnAdd}`}>
                            Agregar Nuevo Curso
                        </button>
                        
                        <div className={styles.tableContainer}>
                            <table className={`${styles.dataTable} ${styles.coursesTable}`}>
                                <thead>
                                    <tr>
                                        <th>Código</th>
                                        <th>Nombre</th>
                                        <th>Créditos</th>
                                        <th>Horas Semanales</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {courses.map(course => (
                                        <tr key={course.codigoCurso}>
                                            <td>{course.codigoCurso}</td>
                                            <td>{course.nombre}</td>
                                            <td>{course.creditos}</td>
                                            <td>{course.horasSemanales}</td>
                                            <td>
                                                <div className={styles.actionButtons}>
                                                    <button 
                                                        onClick={() => openEditModal(course)} 
                                                        className={`${styles.btn} ${styles.btnEdit}`}
                                                    >
                                                        Editar
                                                    </button>
                                                    <button 
                                                        onClick={() => handleDeleteCourse(course.codigoCurso)}
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

            case 'academicCycles':
                return (
                    <div className={styles.crudSection}>
                        <h3>Gestión de Ciclos Académicos</h3>
                        <button onClick={openCreateModal} className={`${styles.btn} ${styles.btnAdd}`}>
                            Agregar Nuevo Ciclo
                        </button>
                        
                        <div className={styles.tableContainer}>
                            <table className={`${styles.dataTable} ${styles.cyclesTable}`}>
                                <thead>
                                    <tr>
                                        <th>Año</th>
                                        <th>Número</th>
                                        <th>Fecha Inicio</th>
                                        <th>Fecha Fin</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {academicCycles.map(cycle => (
                                        <tr key={`${cycle.anio}-${cycle.numero}`}>
                                            <td>{cycle.anio}</td>
                                            <td>{cycle.numero}</td>
                                            <td>{new Date(cycle.fechaInicio).toLocaleDateString()}</td>
                                            <td>{new Date(cycle.fechaFin).toLocaleDateString()}</td>
                                            <td>
                                                <div className={styles.actionButtons}>
                                                    <button 
                                                        onClick={() => openEditModal(cycle)} 
                                                        className={`${styles.btn} ${styles.btnEdit}`}
                                                    >
                                                        Editar
                                                    </button>
                                                    <button 
                                                        onClick={() => handleDeleteCycle(cycle.anio, cycle.numero)}
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

            case 'groups':
                return (
                    <div className={styles.crudSection}>
                        <h3>Gestión de Grupos</h3>
                        <button onClick={openCreateModal} className={`${styles.btn} ${styles.btnAdd}`}>
                            Agregar Nuevo Grupo
                        </button>
                        
                        <div className={styles.tableContainer}>
                            <table className={`${styles.dataTable} ${styles.groupsTable}`}>
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Curso</th>
                                        <th>Profesor</th>
                                        <th>Ciclo</th>
                                        <th>Horario</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {groups.map(group => {
                                        const course = courses.find(c => c.codigoCurso === group.codigoCurso) || {};
                                        const teacher = teachers.find(t => t.cedula === group.cedulaProfesor) || {};
                                        const cycle = academicCycles.find(c => 
                                            c.anio === group.anio && c.numero === group.numeroCiclo
                                        ) || {};
                                        
                                        return (
                                            <tr key={group.grupoId}>
                                                <td>{group.grupoId}</td>
                                                <td>{course.nombre || group.codigoCurso}</td>
                                                <td>{teacher.nombre || group.cedulaProfesor}</td>
                                                <td>{cycle.anio ? `${cycle.anio}-${cycle.numero}` : `${group.anio}-${group.numeroCiclo}`}</td>
                                                <td>{group.horario}</td>
                                                <td>
                                                    <div className={styles.actionButtons}>
                                                        <button 
                                                            onClick={() => openEditModal(group)} 
                                                            className={`${styles.btn} ${styles.btnEdit}`}
                                                        >
                                                            Editar
                                                        </button>
                                                        <button 
                                                            onClick={() => handleDeleteGroup(group.grupoId)}
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

            default:
                return (
                    <div className={styles.dashboardOverview}>
                        <h3>Panel de Administración</h3>
                        <p>Bienvenido al sistema de administración académica.</p>
                        
                        <div className={styles.statsContainer}>
                            <div className={styles.statCard}>
                                <h4>Profesores</h4>
                                <p>{teachers.length} registrados</p>
                            </div>
                            <div className={styles.statCard}>
                                <h4>Alumnos</h4>
                                <p>{students.length} registrados</p>
                            </div>
                            <div className={styles.statCard}>
                                <h4>Cursos</h4>
                                <p>{courses.length} registrados</p>
                            </div>
                            <div className={styles.statCard}>
                                <h4>Ciclos</h4>
                                <p>{academicCycles.length} activos</p>
                            </div>
                            <div className={styles.statCard}>
                                <h4>Grupos</h4>
                                <p>{groups.length} activos</p>
                            </div>
                        </div>
                    </div>
                );
        }
    };

    return (
        <div className={styles.dashboardLayout}>
            <aside className={styles.sidebar}>
                <h2>Administrador</h2>
                <ul>
                    <li 
                        className={activeSection === 'dashboard' ? styles.active : ''}
                        onClick={() => { setActiveSection('dashboard'); setActiveCRUDSection(null); }}
                    >
                        Dashboard
                    </li>
                    <li 
                        className={activeCRUDSection === 'teachers' ? styles.active : ''}
                        onClick={() => { setActiveCRUDSection('teachers'); }}
                    >
                        Profesores
                    </li>
                    <li 
                        className={activeCRUDSection === 'students' ? styles.active : ''}
                        onClick={() => { setActiveCRUDSection('students'); }}
                    >
                        Alumnos
                    </li>
                    <li 
                        className={activeCRUDSection === 'courses' ? styles.active : ''}
                        onClick={() => { setActiveCRUDSection('courses'); }}
                    >
                        Cursos
                    </li>
                    <li 
                        className={activeCRUDSection === 'academicCycles' ? styles.active : ''}
                        onClick={() => { setActiveCRUDSection('academicCycles'); }}
                    >
                        Ciclos Académicos
                    </li>
                    <li 
                        className={activeCRUDSection === 'groups' ? styles.active : ''}
                        onClick={() => { setActiveCRUDSection('groups'); }}
                    >
                        Grupos
                    </li>
                </ul>
            </aside>
            <main className={styles.mainContent}>
                <header className={styles.header}>
                    Bienvenido, Administrador {user.name}
                </header>
                <section>
                    {renderCRUDSection()}
                </section>
            </main>
        </div>
    );
};

export default AdminDashboard;