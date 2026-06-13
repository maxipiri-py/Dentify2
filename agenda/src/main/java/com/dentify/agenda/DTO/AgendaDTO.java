package com.dentify.agenda.DTO;

import lombok.Data;

@Data
public class AgendaDTO {
    private Integer id;
    // Regla de negocio: la agenda DEBE estar asociada a un paciente y a un dentista
    private Integer pacienteId;   // obligatorio
    private Integer dentistaId;   // obligatorio
    private String nombre;
    private String apellido;
    private String rut;
    private String especialidad;
    private String telefono;
    private String email;
    private String fechaHora;     // fecha/hora de la cita
}
