package com.dentify.dentista.DTO;

import lombok.Data;

@Data
public class DentistaDTO {

    private Integer id;
    private String rut;
    private String nombre;
    private String apellido;
    private String especialidad;
    private String correo;
    private String telefono;
}
