CREATE TABLE IF NOT EXISTS reservas (
        id BIGSERIAL PRIMARY KEY,
        fecha DATE NOT NULL,
        hora TIME NOT NULL,
        nombre VARCHAR(100) NOT NULL,
        telefono VARCHAR(25) NOT NULL,
        creado_en TIMESTAMP NOT NULL,
        CONSTRAINT uk_reserva_fecha_hora UNIQUE (fecha, hora)
    );