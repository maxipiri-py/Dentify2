package com.dentify.presupuesto.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dentify.presupuesto.Model.PresupuestoModel;
import com.dentify.presupuesto.Repository.PresupuestoRepository;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class PresupuestoService {

    @Autowired
    private PresupuestoRepository presupuestoRepository;

    @Autowired
    @Qualifier("webClientPaciente")
    private WebClient webClientPaciente;

    @Autowired
    @Qualifier("webClientDentista")
    private WebClient webClientDentista;

    @Autowired
    @Qualifier("webClientPrestaciones")
    private WebClient webClientPrestaciones;

    // ─── Validaciones de datos ────────────────────────────────────────────────

    private void validarRutSiPresente(String rut) {
        if (rut == null || rut.isBlank()) return;
        if (rut.trim().length() > 12) {
            throw new IllegalArgumentException(
                "El RUT no puede tener más de 12 caracteres (incluyendo el guion).");
        }
        if (!rut.trim().matches("^[0-9]{7,8}-[0-9Kk]$")) {
            throw new IllegalArgumentException(
                "Formato de RUT inválido. Use el formato: 12345678-9 o 1234567-K");
        }
    }

    // ─── Verificaciones HTTP entre microservicios ─────────────────────────────

    /**
     * REGLA: El presupuesto debe estar asociado a un paciente existente.
     */
    private void verificarPacienteExiste(Integer pacienteId) {
        if (pacienteId == null) {
            throw new IllegalArgumentException(
                "Regla de negocio: el presupuesto debe tener un paciente. Indique el pacienteId.");
        }
        Boolean existe = webClientPaciente.get()
            .uri("/paciente/{id}", pacienteId)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,
                resp -> Mono.error(new IllegalArgumentException("Paciente no encontrado")))
            .onStatus(HttpStatusCode::is5xxServerError,
                resp -> Mono.error(new RuntimeException("Error en servicio de pacientes")))
            .toBodilessEntity()
            .map(r -> true)
            .onErrorReturn(IllegalArgumentException.class, false)
            .block();

        if (Boolean.FALSE.equals(existe)) {
            throw new IllegalArgumentException(
                "Regla de negocio: no existe el paciente con ID: " + pacienteId);
        }
    }

    /**
     * REGLA: El presupuesto debe estar asociado a un dentista existente.
     */
    private void verificarDentistaExiste(Integer dentistaId) {
        if (dentistaId == null) {
            throw new IllegalArgumentException(
                "Regla de negocio: el presupuesto debe tener un dentista. Indique el dentistaId.");
        }
        Boolean existe = webClientDentista.get()
            .uri("/dentistas/{id}", dentistaId)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,
                resp -> Mono.error(new IllegalArgumentException("Dentista no encontrado")))
            .onStatus(HttpStatusCode::is5xxServerError,
                resp -> Mono.error(new RuntimeException("Error en servicio de dentistas")))
            .toBodilessEntity()
            .map(r -> true)
            .onErrorReturn(IllegalArgumentException.class, false)
            .block();

        if (Boolean.FALSE.equals(existe)) {
            throw new IllegalArgumentException(
                "Regla de negocio: no existe el dentista con ID: " + dentistaId);
        }
    }

    /**
     * REGLA: El presupuesto debe estar asociado a una prestación existente.
     */
    private void verificarPrestacionExiste(Integer prestacionId) {
        if (prestacionId == null) {
            throw new IllegalArgumentException(
                "Regla de negocio: el presupuesto debe tener una prestación. Indique el prestacionId.");
        }
        Boolean existe = webClientPrestaciones.get()
            .uri("/prestaciones/{id}", prestacionId)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,
                resp -> Mono.error(new IllegalArgumentException("Prestación no encontrada")))
            .onStatus(HttpStatusCode::is5xxServerError,
                resp -> Mono.error(new RuntimeException("Error en servicio de prestaciones")))
            .toBodilessEntity()
            .map(r -> true)
            .onErrorReturn(IllegalArgumentException.class, false)
            .block();

        if (Boolean.FALSE.equals(existe)) {
            throw new IllegalArgumentException(
                "Regla de negocio: no existe la prestación con ID: " + prestacionId);
        }
    }

    // ─── CRUD ────────────────────────────────────────────────────────────────

    public Optional<PresupuestoModel> buscarPorId(Integer id) {
        return presupuestoRepository.findById(id);
    }

    public List<PresupuestoModel> buscarPorEstado(String estado) {
        return presupuestoRepository.findByEstadoPago(estado);
    }

    public Boolean guardarPresupuesto(PresupuestoModel presupuesto) {
        // 1. Verificar existencia cruzada entre microservicios
        verificarPacienteExiste(presupuesto.getPacienteId());
        verificarDentistaExiste(presupuesto.getDentistaId());
        verificarPrestacionExiste(presupuesto.getPrestacionId());

        // 2. Validar RUT si viene informado
        validarRutSiPresente(presupuesto.getRutPaciente());

        try {
            presupuestoRepository.save(presupuesto);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
