<<<<<<< HEAD
create table reporte (
    id integer not null auto_increment,
    tipo_reporte varchar(255),
    descripcion text,
    fecha_generacion date,
    total_pacientes integer,
    total_citas integer,
    total_ingresos double,
    primary key (id)
=======
CREATE TABLE reporte (
    id INTEGER NOT NULL AUTO_INCREMENT,
    tipo_reporte VARCHAR(255),
    descripcion TEXT,
    fecha_generacion DATE,
    total_pacientes INTEGER,
    total_citas INTEGER,
    total_ingresos DOUBLE,
    PRIMARY KEY (id)
>>>>>>> 67fa270851138a6dc0b277d1edf3022863cc72e3
);