package com.dentify.pagos.Model;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pago")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = true)
    private Integer pacienteId;

    @Column(nullable = true)
    private String nombrePaciente;

    
    @Column(length = 13, nullable = false) 
    private String rutPaciente;

    @Column(nullable = true)
    private Integer presupuestoId;

    @Column(nullable = true)
    private Double montoPagado;

    @Column(nullable = true)
    private Double montoTotal;

    @Column(nullable = true)
    private LocalDate fechaPago;

    @Column(nullable = true)
    private String metodoPago;  

    @Column(nullable = true)
    private String estadoPago;  
}