# 🎓 Banner-App Ecosystem

Repositorio principal del ecosistema **Banner-App**, una solución académica integral inspirada en plataformas universitarias como Banner. El sistema permite la gestión de estudiantes, cursos, matrículas y ciclos lectivos, y está compuesto por:

- 🧠 API REST en **Kotlin + Spring Boot**
- 🗄️ Base de datos **Oracle Database XE**
- 🌐 Frontend web en **React.js**
- 📱 App móvil en **Android Studio** con **XML Views**

![Arquitectura del sistema](https://raw.githubusercontent.com/Joscalion04/Banner-App/main/Arquitectura_Banner.png)

---

## 🧠 Backend - API Kotlin + Spring Boot

- Proyecto principal que expone endpoints RESTful.
- Responsable de la lógica de negocio, autenticación y validaciones.
- Utiliza **DAOs** para acceder a procedimientos y funciones de Oracle.
- Gestión robusta de excepciones desde la base de datos hacia el cliente.

**Tecnologías**:
- Kotlin
- Spring Boot (Web, Security, JDBC)

---

## 🗄️ Base de Datos - Oracle XE

- Todas las tablas relacionadas con alumnos, carreras, cursos, matrícula, etc.
- Contiene:
  - **Procedimientos** (`PROCEDURE`) para procesos como matrícula, desmatrícula, registro, [CRUDS].
  - **Funciones** (`FUNCTION`) para cálculos de promedio, validaciones, etc.
  - **Manejo de errores** desde procedimientos (uso de `EXCEPTION` Oracle).

**Esquema principal**: `MOVILES`

---

## 🌐 Frontend Web - React.js

- Aplicación SPA para uso en escritorio y dispositivos móviles.
- Consume la API para mostrar datos del estudiante, perfil, matrícula y dashboards personalizados.
- Permite registro, login, matrícula, consulta de cursos y más

**Tecnologías**:
- React.js
- React Router
- CSS Modules
- Fetch API

---

## 📱 App Móvil - Android

- App nativa en Android Studio
- Interfaz diseñada con **XML Views**
- Conectada directamente a la API
- Permite registro, login, matrícula, consulta de cursos y más

**Tecnologías**:
- Kotlin
- Retrofit2
- XML + Navigation Components

---
