package com.dentify.observaciones.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dentify.observaciones.DTO.ObservacionDTO;
import com.dentify.observaciones.Model.Observacion;
import com.dentify.observaciones.Repository.ObservacionRepository;

import reactor.core.publisher.Mono;

@Service
public class ObservacionService {

    @Autowired
    private ObservacionRepository observacionRepository;

    @Autowired
    @Qualifier("webClientAgenda")
    private WebClient webClientAgenda;

    // ─── Verificación HTTP entre microservicios ───────────────────────────────

    /**
     * REGLA: Una observación debe estar vinculada a una agenda (cita) existente.
     */
    private void verificarAgendaExiste(Integer agendaId) {
        if (agendaId == null) {
            throw new IllegalArgumentException(
                "Regla de negocio: la observación debe estar asociada a una agenda. Indique el agendaId.");
        }
        Boolean existe = webClientAgenda.get()
            .uri("/agenda/{id}", agendaId)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,
                resp -> Mono.error(new IllegalArgumentException("Agenda no encontrada")))
            .onStatus(HttpStatusCode::is5xxServerError,
                resp -> Mono.error(new RuntimeException("Error en servicio de agenda")))
            .toBodilessEntity()
            .map(r -> true)
            .onErrorReturn(IllegalArgumentException.class, false)
            .block();

        if (Boolean.FALSE.equals(existe)) {
            throw new IllegalArgumentException(
                "Regla de negocio: no existe la agenda con ID: " + agendaId +
                ". La observación debe asociarse a una cita existente.");
        }
    }

    private void validarDescripcion(ObservacionDTO dto) {
        if (dto.getDescripcion() == null || dto.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripción de la observación no puede estar vacía.");
        }
    }

    // ─── CRUD ────────────────────────────────────────────────────────────────

    public Observacion crear(ObservacionDTO dto) {
        verificarAgendaExiste(dto.getAgendaId());
        validarDescripcion(dto);

        Observacion observacion = new Observacion();
        observacion.setAgendaId(dto.getAgendaId());
        observacion.setDescripcion(dto.getDescripcion().trim());
        observacion.setFecha(LocalDate.now());

        return observacionRepository.save(observacion);
    }

    public List<Observacion> listar() {
        return observacionRepository.findAll();
    }

    public Optional<Observacion> buscarPorId(Integer id) {
        return observacionRepository.findById(id);
    }

    public List<Observacion> buscarPorAgenda(Integer agendaId) {
        return observacionRepository.findByAgendaId(agendaId);
    }

    public Optional<Observacion> actualizar(Integer id, ObservacionDTO dto) {
        Optional<Observacion> observacionOptional = observacionRepository.findById(id);
        if (observacionOptional.isEmpty()) {
            return Optional.empty();
        }

        verificarAgendaExiste(dto.getAgendaId());
        validarDescripcion(dto);

        Observacion observacion = observacionOptional.get();
        observacion.setAgendaId(dto.getAgendaId());
        observacion.setDescripcion(dto.getDescripcion().trim());

        return Optional.of(observacionRepository.save(observacion));
    }

    public boolean eliminar(Integer id) {
        Optional<Observacion> observacion = observacionRepository.findById(id);
        if (observacion.isEmpty()) {
            return false;
        }
        observacionRepository.deleteById(id);
        return true;
    }
}
