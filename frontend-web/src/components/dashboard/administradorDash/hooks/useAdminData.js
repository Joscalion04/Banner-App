import { useEffect, useState } from 'react';
import * as adminApi from '../adminApi';

const useAdminData = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [teachers, setTeachers] = useState([]);
    const [students, setStudents] = useState([]);
    const [courses, setCourses] = useState([]);
    const [academicCycles, setAcademicCycles] = useState([]);
    const [groups, setGroups] = useState([]);
    const [careers, setCareers] = useState([]);
    const [careerCourses, setCareerCourses] = useState([]);
    const [users, setUsers] = useState([]);

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
                    careerCoursesData,
                    usersData 
                ] = await Promise.all([
                    adminApi.obtenerCarreras(),
                    adminApi.obtenerProfesores(),
                    adminApi.obtenerAlumnos(),
                    adminApi.obtenerCursos(),
                    adminApi.obtenerCiclos(),
                    adminApi.obtenerGrupos(),
                    adminApi.obtenerCarrerasCursos(),
                    adminApi.obtenerUsuarios()
                ]);
                
                setCareers(careersData);
                setTeachers(teachersData);
                setStudents(studentsData);
                setCourses(coursesData);
                setAcademicCycles(cyclesData);
                setGroups(groupsData);
                setCareerCourses(careerCoursesData);
                setUsers(usersData);

            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };
        
        loadInitialData();
    }, []);


    const handleDeleteUser = async (cedula) => {
        try {
            setLoading(true);
            await adminApi.eliminarUsuario(cedula);
            setUsers(users.filter(u => u.cedula !== cedula));
            return true;
        } catch (err) {
            if (err.message.includes('registros dependientes')) {
            throw new Error('No se puede eliminar: usuario tiene registros asociados');
            }
            throw err;
        } finally {
            setLoading(false);
        }
    };

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

    // Añade esta función:
    const handleDeleteCareer = async (codigo) => {
    try {
        setLoading(true);
        await adminApi.eliminarCarrera(codigo);
        setCareers(careers.filter(c => c.codigoCarrera !== codigo));
    } catch (err) {
        throw err; // Se maneja en el componente
    } finally {
        setLoading(false);
    }
    };


    const handleDeleteCareerCourse = async (id) => {
        try {
            setLoading(true);
            await adminApi.eliminarCarreraCurso(id);
            setCareerCourses(careerCourses.filter(cc => cc.carreraCursoId !== id));
            return true;
        } catch (err) {
            if (err.message.includes('registros dependientes')) {
            throw new Error('No se puede eliminar: existen registros dependientes');
            }
            throw err;
        } finally {
            setLoading(false);
        }
    };

    return {
        teachers, students, courses, academicCycles, groups,
        careers, users, careerCourses, loading, error,
        handleDeleteTeacher, handleDeleteStudent, handleDeleteCourse,
        handleDeleteCycle, handleDeleteGroup, handleDeleteCareer, handleDeleteUser, handleDeleteCareerCourse
    };
};

export default useAdminData;