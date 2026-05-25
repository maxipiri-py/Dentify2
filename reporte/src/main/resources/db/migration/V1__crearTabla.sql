CREATE TABLE reporte (
    id INTEGER NOT NULL AUTO_INCREMENT,
    tipo_reporte VARCHAR(255),
    descripcion TEXT,
    fecha_generacion DATE,
    total_pacientes INTEGER,
    total_citas INTEGER,
    total_ingresos DOUBLE,
    PRIMARY KEY (id)
);