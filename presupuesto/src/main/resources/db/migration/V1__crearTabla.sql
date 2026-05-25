CREATE TABLE presupuesto (
    id INTEGER NOT NULL AUTO_INCREMENT,
    paciente_id INTEGER,
    nombre_paciente VARCHAR(255),
    rut_paciente VARCHAR(13),
    tratamiento VARCHAR(255),
    descripcion TEXT,
    monto_total DOUBLE,
    fecha_emision DATE,
    estado_pago VARCHAR(100),
    PRIMARY KEY (id)
);