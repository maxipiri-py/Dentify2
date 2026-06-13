package com.dentify.agenda.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dentify.agenda.DTO.AgendaDTO;
import com.dentify.agenda.Model.AgendaModel;
import com.dentify.agenda.Repository.AgendaRepository;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    // WebClients inyectados desde WebConfig
    @Autowired
    @Qualifier("webClientPaciente")
    private WebClient webClientPaciente;

    @Autowired
    @Qualifier("webClientDentista")
    private WebClient webClientDentista;

    // ─── Validaciones de datos ────────────────────────────────────────────────

    private void validarRut(String rut) {
        if (rut == null || rut.isBlank()) {
            throw new IllegalArgumentException("El RUT no puede estar vacío.");
        }
        if (rut.trim().length() > 12) {
            throw new IllegalArgumentException(
                "El RUT no puede tener más de 12 caracteres (incluyendo el guion). Formato: 12345678-9");
        }
        if (!rut.trim().matches("^[0-9]{7,8}-[0-9Kk]$")) {
            throw new IllegalArgumentException(
                "Formato de RUT inválido. Use el formato: 12345678-9 o 1234567-K");
        }
    }

    // ─── Reglas de negocio con llamadas HTTP ─────────────────────────────────

    /**
     * Verifica que el paciente existe en el microservicio de pacientes (puerto 8081).
     * REGLA: No se puede agendar una hora sin paciente válido.
     */
    private void verificarPacienteExiste(Integer pacienteId) {
        if (pacienteId == null) {
            throw new IllegalArgumentException(
                "Regla de negocio: no se puede agendar una hora sin un paciente. Indique el pacienteId.");
        }
        Boolean existe = webClientPaciente.get()
            .uri("/paciente/{id}", pacienteId)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,
                resp -> Mono.error(new IllegalArgumentException(
                    "No existe el paciente con ID: " + pacienteId)))
            .onStatus(HttpStatusCode::is5xxServerError,
                resp -> Mono.error(new RuntimeException(
                    "Error al comunicarse con el servicio de pacientes.")))
            .toBodilessEntity()
            .map(r -> true)
            .onErrorReturn(IllegalArgumentException.class, false)
            .block();

        if (Boolean.FALSE.equals(existe)) {
            throw new IllegalArgumentException(
                "Regla de negocio: no existe el paciente con ID: " + pacienteId +
                ". Registre el paciente antes de agendar.");
        }
    }

    /**
     * Verifica que el dentista existe en el microservicio de dentistas (puerto 8082).
     * REGLA: La hora debe estar asociada a un dentista válido.
     */
    private void verificarDentistaExiste(Integer dentistaId) {
        if (dentistaId == null) {
            throw new IllegalArgumentException(
                "Regla de negocio: la hora debe estar asociada a un dentista. Indique el dentistaId.");
        }
        Boolean existe = webClientDentista.get()
            .uri("/dentistas/{id}", dentistaId)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,
                resp -> Mono.error(new IllegalArgumentException(
                    "No existe el dentista con ID: " + dentistaId)))
            .onStatus(HttpStatusCode::is5xxServerError,
                resp -> Mono.error(new RuntimeException(
                    "Error al comunicarse con el servicio de dentistas.")))
            .toBodilessEntity()
            .map(r -> true)
            .onErrorReturn(IllegalArgumentException.class, false)
            .block();

        if (Boolean.FALSE.equals(existe)) {
            throw new IllegalArgumentException(
                "Regla de negocio: no existe el dentista con ID: " + dentistaId +
                ". Registre el dentista antes de agendar.");
        }
    }

    // ─── CRUD ────────────────────────────────────────────────────────────────

    public List<AgendaDTO> buscarTodos() {
        return agendaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AgendaDTO> buscarPorId(int id) {
        return agendaRepository.findById(id).map(this::convertToDTO);
    }

    public Boolean guardarAgenda(AgendaDTO agenda) {
        // 1. Verificar existencia de paciente y dentista llamando a sus microservicios
        verificarPacienteExiste(agenda.getPacienteId());
        verificarDentistaExiste(agenda.getDentistaId());

        // 2. Validar RUT si viene informado
        if (agenda.getRut() != null && !agenda.getRut().isBlank()) {
            validarRut(agenda.getRut());
        }

        // 3. Nombre no puede ser vacío si viene
        if (agenda.getNombre() != null && agenda.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar en blanco.");
        }

        try {
            agendaRepository.save(convertToModel(agenda));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private AgendaModel convertToModel(AgendaDTO dto) {
        if (dto == null) return null;
        AgendaModel model = new AgendaModel();
        model.setId(dto.getId());
        model.setPacienteId(dto.getPacienteId());
        model.setDentistaId(dto.getDentistaId());
        model.setNombre(dto.getNombre() != null ? dto.getNombre().trim() : null);
        model.setApellido(dto.getApellido() != null ? dto.getApellido().trim() : null);
        model.setRut(dto.getRut() != null ? dto.getRut().trim() : null);
        model.setEspecialidad(dto.getEspecialidad());
        model.setTelefono(dto.getTelefono());
        model.setEmail(dto.getEmail());
        model.setFechaHora(dto.getFechaHora());
        return model;
    }

    private AgendaDTO convertToDTO(AgendaModel model) {
        if (model == null) return null;
        AgendaDTO dto = new AgendaDTO();
        dto.setId(model.getId());
        dto.setPacienteId(model.getPacienteId());
        dto.setDentistaId(model.getDentistaId());
        dto.setNombre(model.getNombre());
        dto.setApellido(model.getApellido());
        dto.setRut(model.getRut());
        dto.setEspecialidad(model.getEspecialidad());
        dto.setTelefono(model.getTelefono());
        dto.setEmail(model.getEmail());
        dto.setFechaHora(model.getFechaHora());
        return dto;
    }
}
