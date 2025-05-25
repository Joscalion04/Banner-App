# Banner-App - API BACKEND

Aplicación web para la gestión académica de estudiantes, inspirada en sistemas universitarios como Banner.

## 📌 Características Principales

## 🛠️ Tecnologías Utilizadas

- **Frontend**:
  - SpringBoot
  - Oracle JDBC
  - Kotlin

## 📁 Arquitectura

![Arquitectura del sistema](https://raw.githubusercontent.com/Joscalion04/Banner-App/main/backend/Backend_Arquitectura.png)

## 📁 Estructura del Proyecto

```plaintext
frontend-web/
├── mvn/wrapper/
├── src/
│   ├── main/
│   │   └── Applicattion.kt
│   │   └── WebConfig.kt
│   │   └── dashboard/
│   │       └── kotlin/com/example/lab4
│   │           ├── controller/ # Controllers de la API
│   │           ├── dao/ # DAOs de la Base de Datos
│   │           └── exceptions/ # Manejo de Excepciones Personalizado
│   │           └── model/ # Modelos de Identidades para el DAO
│   │           └── service/ # Servicios de Proxy para acceso al DAO
│   └── resoruces/application.properties
└── pom.xml
```
