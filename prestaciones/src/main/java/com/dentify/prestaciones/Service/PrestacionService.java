package com.dentify.prestaciones.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dentify.prestaciones.DTO.PrestacionDTO;
import com.dentify.prestaciones.Model.Prestacion;
import com.dentify.prestaciones.Repository.PrestacionRepository;

@Service
public class PrestacionService {

    @Autowired
    private PrestacionRepository prestacionRepository;

    public Prestacion crear(PrestacionDTO dto){

        Prestacion prestacion = new Prestacion();

        prestacion.setNombre(dto.getNombre());
        prestacion.setDescripcion(dto.getDescripcion());
        prestacion.setValor(dto.getValor());

        return prestacionRepository.save(prestacion);
    }

    public List<Prestacion> listar(){

        return prestacionRepository.findAll();
    }
}
