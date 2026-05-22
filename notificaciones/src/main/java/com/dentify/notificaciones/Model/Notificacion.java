package com.dentify.notificaciones.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notificacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String correo;

    private String asunto;

    private String mensaje;

    private String estado;
}
