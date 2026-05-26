package com.dentify.agenda.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dentify.agenda.DTO.AgendaDTO;
import com.dentify.agenda.Model.AgendaModel;
import com.dentify.agenda.Repository.AgendaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AgendaService {
    @Autowired
    private AgendaRepository agendaRepository;

    public void Crear(AgendaDTO agendaDTO){
        AgendaModel model = convertToModel(agendaDTO);
        agendaRepository.save(model);
    }
    public List<AgendaDTO> buscarTodos(){
        return agendaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public Optional<AgendaDTO> buscarPorId(int id){
        return agendaRepository.findById(id)
                .map(this::convertToDTO);
    }
    public List<AgendaDTO> obtenerPorAgenda(Integer agendaId) {
        return agendaRepository.findById(agendaId)
                .map(this::convertToDTO)
                .map(List::of)
                .orElse(List.of());
    }
    public Boolean guardarAgenda(AgendaDTO agenda) {
        try {
            AgendaModel model = convertToModel(agenda);
            agendaRepository.save(model);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private AgendaModel convertToModel(AgendaDTO dto) {
        if (dto == null) return null;
        AgendaModel model = new AgendaModel();
        model.setId(dto.getId());
        model.setNombre(dto.getNombre());
        model.setApellido(dto.getApellido());
        model.setRut(dto.getRun());
        model.setEspecialidad(dto.getEspecialides());
        return model;
    }

    private AgendaDTO convertToDTO(AgendaModel model) {
        if (model == null) return null;
        AgendaDTO dto = new AgendaDTO();
        dto.setId(model.getId());
        dto.setNombre(model.getNombre());
        dto.setApellido(model.getApellido());
        dto.setRun(model.getRut());
        dto.setEspecialides(model.getEspecialidad());
        return dto;
    }
}
