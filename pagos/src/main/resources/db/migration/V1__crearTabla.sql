CREATE TABLE pago (
    id INTEGER NOT NULL AUTO_INCREMENT,
    paciente_id INTEGER,
    nombre_paciente VARCHAR(255),
    rut_paciente VARCHAR(13) NOT NULL,
    presupuesto_id INTEGER,
    monto_pagado DOUBLE,
    monto_total DOUBLE,
    fecha_pago DATE,
    metodo_pago VARCHAR(100),
    estado_pago VARCHAR(100),
    PRIMARY KEY (id)
);