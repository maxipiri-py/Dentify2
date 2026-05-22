package com.dentify.dentista.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dentify.dentista.DTO.DentistaDTO;
import com.dentify.dentista.Model.Dentista;
import com.dentify.dentista.Repository.DentistaRepository;

@Service
public class DentistaService {

    @Autowired
    private DentistaRepository dentistaRepository;

    public Dentista crear(DentistaDTO dentistaDTO) {
        Dentista dentista = new Dentista();
        dentista.setRut(dentistaDTO.getRut());
        dentista.setNombre(dentistaDTO.getNombre());
        dentista.setApellido(dentistaDTO.getApellido());
        dentista.setEspecialidad(dentistaDTO.getEspecialidad());
        dentista.setCorreo(dentistaDTO.getCorreo());
        dentista.setTelefono(dentistaDTO.getTelefono());
        Dentista dentistaSave=dentistaRepository.save(dentista);
        return dentistaSave;
    }

    public Optional<Dentista> buscarPorId(Integer id) {
        return dentistaRepository.findById(id);
    }

    public List<Dentista> listarTodos() {
        return dentistaRepository.findAll();
    }
    
    public List<Dentista> buscarTodos(){
        return dentistaRepository.findAll();
    }
}
