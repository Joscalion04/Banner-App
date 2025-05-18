# 🎓 Banner-App Ecosystem

Repositorio principal del ecosistema **Banner-App**, una solución académica integral inspirada en plataformas universitarias como Banner. El sistema permite la gestión de estudiantes, cursos, matrículas y ciclos lectivos, y está compuesto por:

- 🧠 API REST en **Kotlin + Spring Boot**
- 🗄️ Base de datos **Oracle Database XE**
- 🌐 Frontend web en **React.js**
- 📱 App móvil en **Android Studio** con **XML Views**

---

## 🧠 Backend - API Kotlin + Spring Boot

- Proyecto principal que expone endpoints RESTful.
- Responsable de la lógica de negocio, autenticación y validaciones.
- Utiliza **DAOs** para acceder a procedimientos y funciones de Oracle.
- Gestión robusta de excepciones desde la base de datos hacia el cliente.

**Tecnologías**:
- Kotlin
- Spring Boot (Web, Security, JDBC)

📂 Ver más: [`/lab4/README.md`](./lab4/README.md)

---

## 🗄️ Base de Datos - Oracle XE

- Todas las tablas relacionadas con alumnos, carreras, cursos, matrícula, etc.
- Contiene:
  - **Procedimientos** (`PROCEDURE`) para procesos como matrícula, desmatrícula, registro, [CRUDS].
  - **Funciones** (`FUNCTION`) para cálculos de promedio, validaciones, etc.
  - **Manejo de errores** desde procedimientos (uso de `EXCEPTION` Oracle).

**Esquema principal**: `MOVILES`

📂 Ver más: [`/database/README.md`](./database/README.md)

---

## 🌐 Frontend Web - React.js

- Aplicación SPA para uso en escritorio y dispositivos móviles.
- Consume la API para mostrar datos del estudiante, perfil, matrícula y dashboard.
- Diseño responsive.

**Tecnologías**:
- React.js
- React Router
- CSS Modules
- Fetch API

📂 Ver más: [`/frontend-web/README.md`](./frontend-web/README.md)

---

## 📱 App Móvil - Android

- App nativa en Android Studio
- Interfaz diseñada con **XML Views**
- Conectada directamente a la API
- Permite autenticación, matrícula, consulta de cursos y más

**Tecnologías**:
- Java/Kotlin (según implementación)
- Retrofit2
- XML + Navigation Components

📂 Ver más: [`/mobile-android/README.md`](./mobile-android/README.md)

---
