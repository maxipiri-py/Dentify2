CREATE TABLE agenda (
    id INTEGER NOT NULL AUTO_INCREMENT,
    -- Regla de negocio: obligatorios
    paciente_id INTEGER NOT NULL,
    dentista_id INTEGER NOT NULL,
    nombre VARCHAR(255),
    apellido VARCHAR(255),
    rut VARCHAR(13),
    especialidad VARCHAR(255),
    telefono VARCHAR(255),
    email VARCHAR(255),
    fecha_hora VARCHAR(50),
    PRIMARY KEY (id)
);
