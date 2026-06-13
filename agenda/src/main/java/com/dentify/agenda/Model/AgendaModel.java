package com.dentify.agenda.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "agenda")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgendaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Regla de negocio: obligatorios
    @Column(nullable = false)
    private Integer pacienteId;

    @Column(nullable = false)
    private Integer dentistaId;

    private String nombre;
    private String apellido;
    private String rut;
    private String especialidad;
    private String telefono;
    private String email;
    private String fechaHora;
}
