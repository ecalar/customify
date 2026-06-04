# 🧊 Customify – Configurador 3D de Productos

**Convierte productos en experiencias 3D personalizables.**

Customify es una plataforma SaaS ligera diseñada para pequeños y medianos fabricantes. Permite subir modelos 3D de productos, definir opciones de personalización (colores, materiales) con sus respectivos suplementos de precio, y generar un visor interactivo para que los clientes finales puedan configurar y visualizar su compra en tiempo real.

---

## 🚀 Características Principales

* **Backoffice de Fabricantes:** Panel de administración seguro para gestionar el catálogo de productos y subir archivos de modelos 3D (`.glb` / `.gltf`).
* **Visor 3D Interactivo:** Frontend desacoplado y sin frameworks que renderiza los productos en 3D, permitiendo rotación, zoom y cambio de materiales en tiempo real.
* **Motor de Precios Dinámico:** Cálculo automático del precio total en el visor del cliente en base a las opciones seleccionadas y sus suplementos.
* **Seguridad Stateless:** Autenticación de la API REST mediante JSON Web Tokens (JWT).
* **Diseño Moderno:** Interfaz de usuario basada en el estilo *Glassmorphism* (efectos de cristal translúcido y desenfoque) para una estética premium.
* **Integración Continua:** Pipeline de CI configurado con GitHub Actions para la ejecución automatizada de tests unitarios y compilación.

---

## 🛠️ Stack Tecnológico

| Capa | Tecnología |
| :--- | :--- |
| **Backend** | Java 17, Spring Boot 3.3.x |
| **Seguridad** | Spring Security, JWT |
| **Base de Datos** | MySQL 8.0, Spring Data JPA, Hibernate |
| **Migraciones** | Flyway |
| **Frontend (Visor)** | Vanilla JavaScript, Three.js (r168) |
| **Frontend (Admin)** | Thymeleaf, Bootstrap 5, CSS3 |
| **Testing** | JUnit 5, Mockito |
| **DevOps** | Docker, Docker Compose, GitHub Actions |

---

## 🏗️ Arquitectura del Proyecto

El proyecto está construido bajo una estructura de **Monorepo** que divide claramente las responsabilidades, implementando un enfoque API-First para el núcleo del negocio.

* **Backend API (`/backend`):** Núcleo central que expone endpoints REST seguros. Gestiona el almacenamiento en el sistema de archivos local (`MultipartFile`) y la persistencia de datos relacionales.
* **Frontend Panel de Control (`/admin/**`):** Vistas renderizadas desde el servidor usando Thymeleaf que consumen la propia API REST mediante JavaScript asíncrono.
* **Frontend Visor Cliente (`/frontend/viewer`):** Cliente estático y ligero pensado para ser incrustado en cualquier e-commerce o desplegado de forma independiente.

---

## ⚙️ Instalación y Despliegue Local

Sigue estos pasos para levantar el proyecto en tu entorno de desarrollo local.

**1. Clonar el repositorio**
```bash
git clone [https://github.com/tu-usuario/customify.git](https://github.com/tu-usuario/customify.git)
cd customify
```
**2. Levantar la base de datos con Docker**

Asegúrate de tener el demonio de Docker encendido y ejecuta:

```bash
docker compose up -d
```

Esto levantará un contenedor MySQL en el puerto 3306 con la configuración esperada por la aplicación.

***3. Compilar y ejecutar el Backend***

La aplicación utiliza Flyway, por lo que las tablas de la base de datos se crearán automáticamente al arrancar.
```bash
cd backend
mvn clean install
mvn spring-boot:run
```
***4. Probar el Visor 3D***

Para evitar bloqueos por políticas CORS del navegador (protocolo file://), sirve la carpeta frontend/viewer/ usando una extensión como Live Server en tu IDE, apuntando a http://localhost:5500/index.html?id=1.

👨‍💻 Acerca del Autor

Este proyecto ha sido desarrollado como pieza central de mi portfolio profesional. Representa la unión natural entre mi experiencia técnica previa en Animación 3D y Entornos Interactivos y mi actual especialización como Desarrollador Backend con Java y Spring Boot.

Actualmente me encuentro en la fase final del ciclo de Desarrollo de Aplicaciones Multiplataforma (DAM), buscando una oportunidad de prácticas FCT 100% remotas donde poder aportar valor escribiendo código limpio, escalable y orientado a producto.
