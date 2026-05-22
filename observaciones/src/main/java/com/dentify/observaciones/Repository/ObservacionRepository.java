package com.dentify.observaciones.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dentify.observaciones.Model.Observacion;

@Repository
public interface ObservacionRepository
        extends JpaRepository<Observacion, Integer>{

    List<Observacion> findByAgendaId(Integer agendaId);
}
