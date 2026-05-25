CREATE TABLE agenda (
    id INTEGER NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255),
    apellido VARCHAR(255),
    rut VARCHAR(13) NOT NULL,
    especialidad VARCHAR(255),
    telefono VARCHAR(50),
    email VARCHAR(255),
    PRIMARY KEY (id)
);