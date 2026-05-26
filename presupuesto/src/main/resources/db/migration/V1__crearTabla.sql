create table presupuesto (
    id integer not null auto_increment,
    paciente_id integer,
    nombre_paciente varchar(255),
    rut_paciente varchar(13),
    tratamiento varchar(255),
    descripcion text,
    monto_total double,
    fecha_emision date,
    estado_pago varchar(255),
    primary key (id)
);