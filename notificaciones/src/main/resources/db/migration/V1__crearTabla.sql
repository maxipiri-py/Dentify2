create table notificacion (
    id integer not null auto_increment,
    correo varchar(255),
    asunto varchar(255),
    mensaje text,
    estado varchar(255),
    primary key (id)
);
