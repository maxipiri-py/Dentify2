package com.dentify.pagos.DTO;
 
import lombok.Data;
 
@Data
public class PagoDTO {
 
    private Integer id;
    private Integer pacienteId;
    private String nombrePaciente;
    private String rutPaciente;
    private Integer presupuestoId;
    private String metodoPago;  
    private String estadoPago;
}

