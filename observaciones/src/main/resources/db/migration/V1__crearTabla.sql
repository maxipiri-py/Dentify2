CREATE TABLE observacion (
    id INTEGER NOT NULL AUTO_INCREMENT,
    agenda_id INTEGER,
    descripcion TEXT,
    fecha DATE,
    PRIMARY KEY (id)
);