package com.dentify.observaciones.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dentify.observaciones.DTO.ObservacionDTO;
import com.dentify.observaciones.Model.Observacion;
import com.dentify.observaciones.Repository.ObservacionRepository;

@Service
public class ObservacionService {

    @Autowired
    private ObservacionRepository observacionRepository;

    public Observacion crear(ObservacionDTO dto){

        Observacion observacion = new Observacion();

        observacion.setAgendaId(dto.getAgendaId());
        observacion.setDescripcion(dto.getDescripcion());
        observacion.setFecha(LocalDate.now());

        return observacionRepository.save(observacion);
    }

    public List<Observacion> listar(){

        return observacionRepository.findAll();
    }
}
