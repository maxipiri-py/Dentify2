package com.dentify.presupuesto.PresupuestoDTO;

import java.time.LocalDate;
 
import lombok.Data;
 
@Data
public class PresupuestoDTO {
 
    private Integer id;
    private Integer pacienteId;
    private String nombrePaciente;
    private String rutPaciente;
    private String tratamiento;
    private String descripcion;
    private Double montoTotal;
    private LocalDate fechaEmision;
    private String estadoPago;
}
 