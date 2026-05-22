package com.dentify.prestaciones.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dentify.prestaciones.Model.Prestacion;

@Repository
public interface PrestacionRepository
        extends JpaRepository<Prestacion, Integer>{

    List<Prestacion> findByNombre(String nombre);
}
