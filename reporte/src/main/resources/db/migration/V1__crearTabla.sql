  create table paciente (
        id integer not null auto_increment,
        fecha_nacimiento datetime(6),
        rut varchar(13) not null,
        apellido varchar(255),
        correo varchar(255),
        nombre varchar(255),
        primary key (id)
    ) 