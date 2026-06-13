package com.dentify.presupuesto.Model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "presupuesto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PresupuestoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Regla de negocio: obligatorios
    @Column(nullable = false)
    private Integer pacienteId;

    @Column(nullable = false)
    private Integer dentistaId;

    @Column(nullable = false)
    private Integer prestacionId;

    private String nombrePaciente;

    @Column(length = 13)
    private String rutPaciente;

    private String tratamiento;
    private String descripcion;
    private Double montoTotal;
    private LocalDate fechaEmision;
    private String estadoPago;
}
