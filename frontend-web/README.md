# Banner-App - Frontend Web

Aplicación web para la gestión académica de estudiantes, inspirada en sistemas universitarios como Banner.

## 📌 Características Principales

- 📚 **Dashboard del estudiante**:
  - Visualización de cursos matriculados
  - Matrícula y desmatrícula de cursos
  - Resumen académico (promedio, cursos aprobados)
- 👤 **Perfil del Estudiante**
- 👤 **Perfil del Profesor**
- 👤 **Perfil del Adminstrador**

## 🛠️ Tecnologías Utilizadas

- **Frontend**:
  - React.js
  - React Router
  - JavaScript (ES6+)
  - CSS Modules
  - Fetch API

## 📁 Estructura del Proyecto

```plaintext
frontend-web/
├── public/
├── src/
│   ├── components/ # Estructura Basica para cada Dashboard
│   │   └── dashboard/
│   │       └── alumnoDash/
│   │           ├── AlumnoDashboard.js        # Componente principal
│   │           ├── alumnoApi.js              # Servicios API
│   │           └── components/               # Subcomponentes
│   │               ├── Sidebar.js
│   │               ├── CursosSection.js
│   │               └── ...
│   ├── pages/
│   ├── styles/
│   ├── App.js
│   └── index.js
├── package.json
└── README.md



## ⚙️ Configuración del Entorno
1. **Requisitos previos**:
   - Node.js (v14 o superior)
   - npm

2. **Instalación**:
   ```bash
   git clone 
   cd frontend-web
   npm install
