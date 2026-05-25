CREATE TABLE notificacion (
    id INTEGER NOT NULL AUTO_INCREMENT,
    correo VARCHAR(255),
    asunto VARCHAR(255),
    mensaje TEXT,
    estado VARCHAR(100),
    PRIMARY KEY (id)
);