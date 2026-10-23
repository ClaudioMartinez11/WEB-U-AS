# Sistema de Reservas

Aplicación web para gestionar reservas de turnos, desarrollada con un frontend HTML/CSS/JavaScript y un backend basado en Spring Boot.

## Características

- Creación de reservas desde el sitio web.
- Validación de fecha y hora.
- Prevención de reservas duplicadas.
- Persistencia de datos mediante SQLite.
- Exportación de reservas a Excel.
- API REST.
- Panel de administración protegido.
- Comunicación mediante HTTPS en producción.

## Tecnologías

### Frontend
- HTML5
- CSS3
- JavaScript
- Fetch API

### Backend
- Java
- Spring Boot
- Spring Security
- SQLite
- Maven
- Apache POI para exportación a Excel

## Estructura

```text
WEB-U-AS/
├── frontend/
│   ├── index.html
│   ├── style.css
│   └── script.js
│
└── backend/
    ├── src/
    ├── pom.xml
    └── ...
```

## Instalación y ejecución

Desde la carpeta del backend:

```bash
mvn clean package
```

Luego:

```bash
java -jar target/reservas-backend-0.0.1-SNAPSHOT.jar
```

La aplicación crea automáticamente la carpeta `data/` y la base de datos SQLite cuando es necesario.

## API

### Crear una reserva

Este endpoint es público y es utilizado por el formulario del sitio.

```http
POST /api/reservas
Content-Type: application/json
```

Ejemplo:

```json
{
  "fecha": "2026-09-05",
  "hora": "09:00",
  "nombre": "Ana Perez",
  "telefono": "11 1234-5678"
}
```

Una reserva creada correctamente responde con:

```http
201 Created
```

### Consultar reservas

```http
GET /api/reservas
```

Este endpoint está destinado exclusivamente al administrador y requiere autenticación.

### Exportar reservas

```http
GET /api/reservas/export
```

Genera y descarga el archivo `reservas.xlsx`.

Este endpoint requiere autenticación de administrador.

## Seguridad

Las operaciones administrativas están protegidas mediante autenticación.

Los datos de acceso del administrador **no se almacenan en el código fuente ni en el repositorio**. Se configuran mediante variables de entorno:

```text
ADMIN_USERNAME=admin
ADMIN_PASSWORD=una-contraseña-segura
```

En producción, estas variables deben configurarse directamente en el servicio de hosting.

Nunca se deben subir contraseñas, tokens, claves API u otros secretos a GitHub.

### Endpoints públicos

```text
POST /api/reservas
```

### Endpoints protegidos

```text
GET /api/reservas
GET /api/reservas/export
```

Los datos personales de las reservas deben permanecer accesibles únicamente para el administrador.

## Reservas duplicadas

La aplicación utiliza una restricción única en SQLite para impedir que dos reservas ocupen simultáneamente la misma fecha y hora.

Si el horario ya está ocupado, la API responde:

```http
409 Conflict
```

Las solicitudes con datos inválidos responden:

```http
400 Bad Request
```

## Producción

Para desplegar la aplicación en producción se recomienda:

- Utilizar HTTPS.
- Configurar las credenciales mediante variables de entorno.
- No almacenar secretos en GitHub.
- Mantener protegidos los endpoints administrativos.
- Validar los datos también en el backend.
- Aplicar límites de solicitudes para evitar spam.
- No exponer nombres ni teléfonos de clientes en endpoints públicos.
- Realizar copias de seguridad de la base de datos.

## Frontend

Con el backend activo, abrir:

```text
index.html
```

El formulario utiliza `fetch` para comunicarse con la API.

El canal de contacto por WhatsApp funciona de manera independiente de la API de reservas.

## Estado del proyecto

Proyecto en desarrollo. La aplicación está orientada a la gestión de reservas y puede desplegarse en un entorno de producción después de configurar correctamente las medidas de seguridad y las variables de entorno.
