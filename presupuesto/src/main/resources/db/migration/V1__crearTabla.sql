CREATE TABLE presupuesto (
    id INTEGER NOT NULL AUTO_INCREMENT,
    -- Regla de negocio: obligatorios
    paciente_id INTEGER NOT NULL,
    dentista_id INTEGER NOT NULL,
    prestacion_id INTEGER NOT NULL,
    nombre_paciente VARCHAR(255),
    rut_paciente VARCHAR(13),
    tratamiento VARCHAR(255),
    descripcion TEXT,
    monto_total DOUBLE,
    fecha_emision DATE,
    estado_pago VARCHAR(255),
    PRIMARY KEY (id)
);
