import { useState } from 'react';
import styles from '../../styles/App.module.css';
import Sidebar from './components/Sidebar';
import DashboardOverview from './components/DashboardOverview';
import TeachersSection from './components/TeachersSection';
import StudentsSection from './components/StudentsSection';
import CoursesSection from './components/CoursesSection';
import CyclesSection from './components/CyclesSection';
import GroupsSection from './components/GroupsSection';
import CareersSection from './components/CareersSection';
import CareerCourseSection from './components/CareerCourseSection';
import UsersSection from './components/UsersSection';
import useAdminData from './hooks/useAdminData';

const AdminDashboard = ({ user , onLogout}) => {
    const [activeSection, setActiveSection] = useState('dashboard');
    const [activeCRUDSection, setActiveCRUDSection] = useState(null);
    
    const {
        teachers, students, courses, academicCycles, groups,
        careers, users, careerCourses, loading, error,
        handleDeleteTeacher, handleDeleteStudent, handleDeleteCourse,
        handleDeleteCycle, handleDeleteGroup, handleDeleteCareer, handleDeleteUser, handleDeleteCareerCourse
    } = useAdminData();

    const renderCRUDSection = () => {
        if (loading) return <div className={styles.loading}>Cargando...</div>;
        if (error) return <div className={styles.error}>Error: {error}</div>;

        switch(activeCRUDSection) {
            case 'users':
                return <UsersSection 
                    users={users} 
                    onDelete={handleDeleteUser} 
                />;
            case 'teachers':
                return <TeachersSection 
                    teachers={teachers} 
                    onDelete={handleDeleteTeacher} 
                />;
            case 'students':
                return <StudentsSection 
                    students={students} 
                    careers={careers} 
                    onDelete={handleDeleteStudent} 
                />;
            case 'courses':
                return <CoursesSection 
                    courses={courses} 
                    onDelete={handleDeleteCourse} 
                />;
            case 'academicCycles':
                return <CyclesSection 
                    cycles={academicCycles} 
                    onDelete={handleDeleteCycle} 
                />;
            case 'groups':
                return <GroupsSection 
                    groups={groups} 
                    courses={courses} 
                    teachers={teachers} 
                    cycles={academicCycles} 
                    onDelete={handleDeleteGroup} 
                />;
            case 'careers':
                return <CareersSection 
                    careers={careers} 
                    onDelete={handleDeleteCareer} 
                />;
            case 'careerCourses':
                return <CareerCourseSection 
                    careerCourses={careerCourses} 
                    careers={careers}
                    courses={courses}
                    onDelete={handleDeleteCareerCourse} 
                />;
            default:
                return <DashboardOverview 
                    teachers={teachers} 
                    students={students} 
                    courses={courses} 
                    cycles={academicCycles} 
                    groups={groups} 
                />;
        }
    };

    return (
        <div className={styles.dashboardLayout}>
            <Sidebar 
                activeSection={activeSection}
                activeCRUDSection={activeCRUDSection}
                setActiveSection={setActiveSection}
                setActiveCRUDSection={setActiveCRUDSection}
                onLogout={onLogout}
            />
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