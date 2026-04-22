
# SGID-JAVA - Sistema de Gestión de Inventarios de Bodegas

**Autores:** Benavides Michael,David Tapia, Kevin Dias 
**Paralelo:** BM  
**Institución:** Universidad Politécnica Estatal del Carchi (UPEC)  
**Proyecto:** Módulo de Gestión de Clientes 

---

##  Descripción del Proyecto
Este sistema representa la implementación en **Java** del Sistema de Gestión de Inventarios (SGID). Se ha diseñado bajo el paradigma de **Programación Orientada a Objetos (POO)** y una arquitectura basada en capas para garantizar la integridad y escalabilidad del software.

##  Arquitectura del Sistema (Capas)
1. **Capa de Interfaz (UI):** Interfaces desarrolladas en Java Swing / JSP / Thymeleaf (según tu versión).
2. **Capa de Control:** Servlets o Controllers que gestionan la lógica de navegación.
3. **Capa de Negocio (Service/Model):** Clases Java con validaciones de RUC (13 dígitos) y encapsulamiento.
4. **Capa de Datos (DAO/Repository):** Conexión JDBC o JPA para la persistencia en base de datos.

---

##  Guía de Ejecución (Terminal / IDE)

Siga estos pasos para compilar y ejecutar el proyecto:

### 1. Compilación (Si usa Maven)
```bash
mvn clean install
````

### 2\. Ejecución del Proyecto

Si usa el JAR generado:

```bash
java -jar target/sgid-java-0.0.1-SNAPSHOT.jar
```

*Si usa un IDE (IntelliJ o Eclipse), simplemente ejecute la clase principal `Main.java`.*

-----

## 🌐 Configuración y Acceso

### Conexión a Base de Datos

Asegúrese de configurar sus credenciales en el archivo correspondiente (`application.properties` o clase de conexión):

  * **DB Name:** sgid\_db\_java
  * **Puerto:** 5432 (PostgreSQL) o 3306 (MySQL)

### Direcciones de Acceso (Si es Web)

  * **Página de Inicio:** [http://localhost:8080/clientes](https://www.google.com/search?q=http://localhost:8080/clientes)
  * **Nuevo Cliente:** [http://localhost:8080/clientes/nuevo](https://www.google.com/search?q=http://localhost:8080/clientes/nuevo)

-----

## 📂 Organización de Paquetes

  * `com.sgid.models`: Definición de clases (Entidades).
  * `com.sgid.services`: Lógica de validación de RUC y reglas de negocio.
  * `com.sgid.controllers`: Manejo de peticiones.
  * `com.sgid.repository`: Interfaces para acceso a datos.

