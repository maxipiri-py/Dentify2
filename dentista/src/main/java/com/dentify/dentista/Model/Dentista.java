package com.dentify.dentista.Model;
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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dentista")
public class Dentista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, length = 13, nullable = false)
    private String rut;

    @Column(nullable = true)
    private String nombre;
    @Column(nullable = true)
    private String apellido;
    @Column(nullable = true)
    private String especialidad;
    @Column(nullable = true)
    private String correo;
    @Column(nullable = true)
    private String telefono;
}
