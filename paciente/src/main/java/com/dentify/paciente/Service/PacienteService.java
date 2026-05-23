package com.dentify.paciente.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dentify.paciente.DTO.PacienteDTO;
import com.dentify.paciente.Model.PacienteModel;
import com.dentify.paciente.Repository.PacienteRepository;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;
    //@Autowired
    // @Qualifier("webClientUsuarios")
    // private WebClient webClient;

    public List<PacienteModel> buscarTodos(){
        return pacienteRepository.findAll();
    }

    public PacienteModel crear(PacienteDTO pacienteDTO){
        PacienteModel paciente= new PacienteModel();
        paciente.setApellido(pacienteDTO.getApellido());
        paciente.setNombre(pacienteDTO.getNombre());
        paciente.setRut(pacienteDTO.getRut());
        paciente.setFechaNacimiento(pacienteDTO.getFechaNacimiento());
        paciente.setCorreo(pacienteDTO.getCorreo());
       

        PacienteModel pacienteSave=pacienteRepository.save(paciente);
        return pacienteSave;
    }

    public PacienteModel obtenerHistorialCompleto(Integer id) {
        return pacienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
    }

    public List<PacienteModel> listarTodos() {
        return pacienteRepository.findAll();
    }
}


