create table agenda (
    id integer not null auto_increment,
    nombre varchar(255),
    apellido varchar(255),
    rut varchar(13),
    especialidad varchar(255),
    telefono varchar(255),
    email varchar(255),
    primary key (id)
);