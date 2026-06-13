package com.dentify.pagos.DTO;

import java.time.LocalDate;
import lombok.Data;

@Data
public class PagoDTO {
    private Integer id;
    private Integer pacienteId;
    private String nombrePaciente;
    private String rutPaciente;
    private Integer presupuestoId;
    

    private Double montoPagado;
    private Double montoTotal;
    private LocalDate fechaPago;
    
    private String metodoPago;  
    private String estadoPago;
}