CREATE TABLE dentista (
    id INTEGER NOT NULL AUTO_INCREMENT,
    rut VARCHAR(13) NOT NULL UNIQUE,
    nombre VARCHAR(255),
    apellido VARCHAR(255),
    especialidad VARCHAR(255),
    correo VARCHAR(255),
    telefono VARCHAR(50),
    PRIMARY KEY (id)
);