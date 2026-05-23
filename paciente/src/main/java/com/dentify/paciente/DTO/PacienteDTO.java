package com.dentify.paciente.DTO;

import java.util.Date;

import lombok.Data;

@Data
public class PacienteDTO {
    
    private String nombre;
    private String rut;
    private String apellido;
    private Date fechaNacimiento;
    private String correo;

}
