package com.dentify.prestaciones.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prestacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prestacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    private String descripcion;

    private Integer valor;
}