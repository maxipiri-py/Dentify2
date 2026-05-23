package com.dentify.reporte.DTO;
 
import java.time.LocalDate;
 
import lombok.Data;
 
@Data
public class ReporteDTO {
 
    private Integer id;
    private String tipoReporte;     
    private String descripcion;
    private LocalDate fechaGeneracion;
    private Integer totalPacientes;
    private Integer totalCitas;
    private Double totalIngresos;
}
 