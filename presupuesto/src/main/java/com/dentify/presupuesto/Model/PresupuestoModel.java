package com.dentify.presupuesto.Model;
 
import java.time.LocalDate;
 
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Entity
@Table(name = "presupuesto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PresupuestoModel {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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