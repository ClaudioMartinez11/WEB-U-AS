IF OBJECT_ID('dbo.reservas', 'U') IS NULL
BEGIN
    CREATE TABLE reservas (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        fecha DATE NOT NULL,
        hora TIME NOT NULL,
        nombre NVARCHAR(100) NOT NULL,
        telefono NVARCHAR(25) NOT NULL,
        creado_en DATETIME NOT NULL,
        CONSTRAINT uk_reserva_fecha_hora UNIQUE (fecha, hora)
    );
END;