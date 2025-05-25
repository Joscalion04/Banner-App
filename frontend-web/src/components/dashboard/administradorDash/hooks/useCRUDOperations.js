import Swal from 'sweetalert2';
import * as adminApi from '../adminApi';

const useCRUDOperations = (entityType, { 
    careers = [], 
    courses = [], 
    teachers = [], 
    academicCycles = [] 
} = {}) => {
    const getEntityName = () => {
        switch(entityType) {
            case 'teachers': return 'Profesor';
            case 'students': return 'Alumno';
            case 'courses': return 'Curso';
            case 'academicCycles': return 'Ciclo';
            case 'groups': return 'Grupo';
            default: return '';
        }
    };

    const renderSweetAlertForm = (data = {}) => {
        const formData = data || {};
        let formHTML = '';
        
        switch(entityType) {
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

            case 'users':
                formHTML = `
                    <div class="sweet-form-group">
                    <label>Cédula:</label>
                    <input type="text" id="swal-cedula" class="swal2-input" 
                        value="${formData.cedula || ''}" placeholder="Cédula" required >
                    </div>
                    <div class="sweet-form-group">
                    <label>Contraseña:</label>
                    <input type="password" id="swal-clave" class="swal2-input" 
                        value="${formData.clave || ''}" placeholder="Contraseña" required>
                    </div>
                    <div class="sweet-form-group">
                    <label>Tipo de Usuario:</label>
                    <select id="swal-tipoUsuario" class="swal2-input" required>
                        <option value="">Seleccione...</option>
                        <option value="ADMINISTRADOR" ${formData.tipoUsuario === 'admin' ? 'selected' : ''}>Administrador</option>
                        <option value="PROFESOR" ${formData.tipoUsuario === 'profesor' ? 'selected' : ''}>Profesor</option>
                        <option value="ALUMNO" ${formData.tipoUsuario === 'alumno' ? 'selected' : ''}>Alumno</option>
                    </select>
                    </div>
                `;
                break;

            case 'careers':
                formHTML = `
                    <div class="sweet-form-group">
                    <label>Código:</label>
                    <input type="text" id="swal-codigoCarrera" class="swal2-input"  value="${formData.codigoCarrera || ''}" placeholder="Código" required>
                    </div>
                    <div class="sweet-form-group">
                    <label>Nombre:</label>
                    <input type="text" id="swal-nombre" class="swal2-input" 
                        value="${formData.nombre || ''}" placeholder="Nombre" required>
                    </div>
                    <div class="sweet-form-group">
                    <label>Título:</label>
                    <input type="text" id="swal-titulo" class="swal2-input" 
                        value="${formData.titulo || ''}" placeholder="Título otorgado" required>
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

    const handleSweetAlertSubmit = async (isEditing, originalData = {}) => {
        try {
            let formData = {};
            
            switch(entityType) {
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
                
                case 'careers':
                    formData = {
                        codigoCarrera: document.getElementById('swal-codigoCarrera').value,
                        nombre: document.getElementById('swal-nombre').value,
                        titulo: document.getElementById('swal-titulo').value
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
                
                case 'users':
                    formData = {
                        cedula: document.getElementById('swal-cedula').value,
                        clave: document.getElementById('swal-clave').value,
                        tipoUsuario: document.getElementById('swal-tipoUsuario').value
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
            
            switch(entityType) {
                case 'teachers':
                    if (isEditing) {
                        await adminApi.actualizarProfesor(formData);
                    } else {
                        result = await adminApi.insertarProfesor(formData);
                    }
                    break;
                case 'students':
                    if (isEditing) {
                        await adminApi.actualizarAlumno(formData);
                    } else {
                        result = await adminApi.insertarAlumno(formData);
                    }
                    break;
                case 'courses':
                    if (isEditing) {
                        await adminApi.actualizarCurso(formData);
                    } else {
                        result = await adminApi.insertarCurso(formData);
                    }
                    break;
                
                case 'careers':
                    if (isEditing) {
                        await adminApi.actualizarCarrera(formData);
                    } else {
                        result = await adminApi.insertarCarrera(formData);
                    }
                    break;
                    
                case 'academicCycles':
                    if (isEditing) {
                        await adminApi.actualizarCiclo(formData);
                    } else {
                        result = await adminApi.insertarCiclo(formData);
                    }
                    break;
                case 'groups':
                    if (isEditing) {
                        await adminApi.actualizarGrupo(formData);
                    } else {
                        result = await adminApi.insertarGrupo(formData);
                    }
                    break;
                
                case 'users':
                    if (isEditing) {
                        await adminApi.actualizarUsuario(formData);
                    } else {
                        result = await adminApi.insertarUsuario(formData);
                    }
                    break;

                default:
                    break;
            }
            
            return true;
        } catch (err) {
            Swal.showValidationMessage(`Error: ${err.message}`);
            return false;
        }
    };

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

    return { openCreateModal, openEditModal };
};

export default useCRUDOperations;