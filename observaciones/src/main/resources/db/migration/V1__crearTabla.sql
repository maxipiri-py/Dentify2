create table observacion (
    id integer not null auto_increment,
    agenda_id integer,
    descripcion text,
    fecha date,
    primary key (id)
);
