create table dentista (
        id integer not null auto_increment,
        rut varchar(12) not null,
        nombre varchar(255) not null,
        apellido varchar(255) not null,
        especialidad varchar(255) not null,
        correo varchar(255),
        telefono varchar(20),
        primary key (id)
    ) 