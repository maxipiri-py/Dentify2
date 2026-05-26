create table reporte (
    id integer not null auto_increment,
    tipo_reporte varchar(255),
    descripcion text,
    fecha_generacion date,
    total_pacientes integer,
    total_citas integer,
    total_ingresos double,
    primary key (id)
);