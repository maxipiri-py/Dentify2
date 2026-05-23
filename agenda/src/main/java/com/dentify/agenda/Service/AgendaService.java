package com.dentify.agenda.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dentify.agenda.DTO.AgendaDTO;
import com.dentify.agenda.Repository.AgendaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AgendaService {
    @Autowired
    private static AgendaRepository agendaRepository;

    public void Crear(AgendaDTO agenda){
        agendaRepository.save(agenda);
    }
    public List<AgendaDTO> buscarTodos(){
        return agendaRepository.findAll();
    }
    public static Optional<AgendaDTO> buscarPorId(int id){
        return agendaRepository.findById(id);
    }
    public List<AgendaDTO> obtenerPorAgenda(Integer agendaId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerPorAgenda'");
    }
    public Boolean guardarAgenda(AgendaDTO agenda) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'guardarAgenda'");
    }
}
