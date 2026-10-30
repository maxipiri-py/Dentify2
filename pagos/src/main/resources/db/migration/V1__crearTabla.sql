CREATE TABLE pago (
    id INTEGER NOT NULL AUTO_INCREMENT,
    paciente_id INTEGER,
    nombre_paciente VARCHAR(255),
    rut_paciente VARCHAR(13),
    presupuesto_id INTEGER,
    metodo_pago VARCHAR(100),
    estado_pago VARCHAR(100),
    PRIMARY KEY (id)
);