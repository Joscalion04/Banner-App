# 📘 Base de Datos - Sistema Académico `MOVILES`

Este esquema de base de datos en Oracle modela un sistema académico universitario. Permite la gestión de carreras, cursos, ciclos, profesores, alumnos, matrícula y control de grupos.

---

## 🧩 Estructura de Tablas

### `CARRERA`
Información de las carreras ofrecidas por la institución.
- `CODIGO_CARRERA` (PK): Código único
- `NOMBRE`: Nombre de la carrera
- `TITULO`: Título que otorga

---

### `CURSO`
Catálogo de cursos disponibles.
- `CODIGO_CURSO` (PK): Código único
- `NOMBRE`: Nombre del curso
- `CREDITOS`: Créditos académicos
- `HORAS_SEMANALES`: Horas de clase por semana

---

### `CARRERA_CURSO`
Relación muchos-a-muchos entre carreras y cursos, indicando en qué ciclo y año se ofrece cada curso.
- `CARRERA_CURSO_ID` (PK): Identificador autogenerado
- `CODIGO_CARRERA` (FK): Carrera
- `CODIGO_CURSO` (FK): Curso
- `ANIO`: Año sugerido
- `CICLO`: Ciclo sugerido
- `ORDEN`: Orden recomendado

---

### `PROFESOR`
Datos del personal docente.
- `CEDULA` (PK): Identificador único
- `NOMBRE`: Nombre completo
- `TELEFONO`: Teléfono de contacto
- `EMAIL`: Correo electrónico

---

### `ALUMNO`
Información de los estudiantes activos.
- `CEDULA` (PK): Identificador único
- `NOMBRE`: Nombre completo
- `TELEFONO`: Teléfono
- `EMAIL`: Correo electrónico
- `FECHA_NACIMIENTO`: Fecha de nacimiento
- `CODIGO_CARRERA` (FK): Carrera a la que pertenece

---

### `CICLO`
Ciclos lectivos (semestres).
- `CICLO_ID` (PK): Identificador autogenerado
- `ANIO`: Año académico
- `NUMERO`: Número de ciclo (1 o 2)
- `FECHA_INICIO`: Fecha de inicio
- `FECHA_FIN`: Fecha de cierre
- `ACTIVO`: Ciclo activo (0 o 1)

---

### `GRUPO`
Representa un grupo específico de un curso en un ciclo.
- `GRUPO_ID` (PK): Identificador autogenerado
- `CICLO_ID` (FK): Ciclo correspondiente
- `CODIGO_CURSO` (FK): Curso impartido
- `NUMERO_GRUPO`: Número del grupo (e.g., 1, 2, 3)
- `HORARIO`: Información del horario
- `CEDULA_PROFESOR` (FK): Profesor asignado

---

### `USUARIO`
Control de acceso para el sistema.
- `CEDULA` (PK): Asociado a una persona
- `CLAVE`: Contraseña cifrada
- `TIPO_USUARIO`: Rol (`ADMINISTRADOR`, `MATRICULADOR`, `PROFESOR`, `ALUMNO`)

---

### `MATRICULA`
Registro de matrícula de alumnos a grupos.
- `MATRICULA_ID` (PK): Identificador autogenerado
- `GRUPO_ID` (FK): Grupo en el que se matricula
- `CEDULA_ALUMNO` (FK): Estudiante matriculado
- `NOTA`: Calificación final (opcional)

---

## 🔗 Relaciones Clave

- `CARRERA` ↔ `ALUMNO` → 1:N
- `CARRERA` ↔ `CARRERA_CURSO` → 1:N
- `CURSO` ↔ `CARRERA_CURSO` → 1:N
- `CURSO` ↔ `GRUPO` → 1:N
- `CICLO` ↔ `GRUPO` → 1:N
- `PROFESOR` ↔ `GRUPO` → 1:N
- `ALUMNO` ↔ `MATRICULA` → 1:N
- `GRUPO` ↔ `MATRICULA` → 1:N
- `USUARIO` usa `CEDULA` como FK lógica para representar usuarios del sistema.

---

## 🛠 Requisitos

- Oracle Database (ExpressEdition 12c)
- Usuario con privilegios para crear objetos en el esquema `MOVILES` (Ejecutar Script de Super Usuario)

## 📥 Ejecución

1. Abrir Oracle SQL Developer o tu cliente preferido.
2. Ejecutar el script `CREATE TABLE` en orden o completo.
3. Verificar con:
   ```sql
   SELECT table_name FROM all_tables WHERE owner = 'MOVILES';
