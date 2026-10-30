-- Completa la tabla pago creada en V1 con los montos, la fecha y el RUT obligatorio
ALTER TABLE pago ADD COLUMN monto_pagado DOUBLE;
ALTER TABLE pago ADD COLUMN monto_total DOUBLE;
ALTER TABLE pago ADD COLUMN fecha_pago DATE;
ALTER TABLE pago MODIFY rut_paciente VARCHAR(13) NOT NULL;
