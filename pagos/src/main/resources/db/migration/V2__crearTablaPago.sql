create table pago (
    id integer not null auto_increment,
    paciente_id integer,
    nombre_paciente varchar(255),
    rut_paciente varchar(13),
    presupuesto_id integer,
    metodo_pago varchar(255),
    estado_pago varchar(255),
    primary key (id)
);
