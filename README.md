# Backend de reservas

Backend Spring Boot para el formulario de citas. Usa SQLite como almacenamiento local y genera un Excel actualizado después de cada reserva.

## Requisitos

- JDK 17 o posterior (el workspace incluye un JDK 26 y se detectó JDK 25).
- Maven 3.9 o posterior. No está instalado actualmente en este equipo; el proyecto incluye un `pom.xml` listo para ejecutarse cuando Maven esté disponible.

## Instalación y ejecución

Desde esta carpeta:

```powershell
mvn clean package
java -jar target/reservas-backend-0.0.1-SNAPSHOT.jar
```

La primera ejecución crea la carpeta `data/`, la base `data/reservas.db` y el archivo `data/reservas.xlsx` al exportar o crear una reserva.

## API

Crear una reserva:

```http
POST http://localhost:8080/api/reservas
Content-Type: application/json

{"fecha":"2026-09-05","hora":"09:00","nombre":"Ana Perez","telefono":"11 1234-5678"}
```

Responde `201 Created` con:

```json
{"id":1,"fecha":"2026-09-05","hora":"09:00","nombre":"Ana Perez","telefono":"11 1234-5678"}
```

`GET /api/reservas` lista las reservas ocupadas. `GET /api/reservas/export` regenera y descarga `reservas.xlsx`.

Las solicitudes inválidas responden `400` con `{"error":"..."}`. Una fecha y hora ya ocupadas responden `409` con el mismo formato. La restricción única de SQLite evita duplicados incluso ante solicitudes concurrentes.

## Frontend

Con el backend activo, abre `index.html` en el navegador y confirma una cita. El formulario usa `fetch` contra `http://localhost:8080/api/reservas`; WhatsApp queda disponible únicamente como canal de contacto separado.