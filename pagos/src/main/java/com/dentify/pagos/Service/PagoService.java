package com.dentify.pagos.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dentify.pagos.DTO.PagoDTO;
import com.dentify.pagos.Model.PagoModel;
import com.dentify.pagos.Repository.PagoRepository;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    @Qualifier("webClientPresupuesto")
    private WebClient webClientPresupuesto;

    // ─── Validaciones de datos ────────────────────────────────────────────────

    private void validarRut(String rut) {
        if (rut == null || rut.isBlank()) {
            throw new IllegalArgumentException("El RUT del paciente no puede estar vacío.");
        }
        if (rut.trim().length() > 12) {
            throw new IllegalArgumentException(
                "El RUT no puede tener más de 12 caracteres (incluyendo el guion).");
        }
        if (!rut.trim().matches("^[0-9]{7,8}-[0-9Kk]$")) {
            throw new IllegalArgumentException(
                "Formato de RUT inválido. Use el formato: 12345678-9 o 1234567-K");
        }
    }

    private void validarMontos(PagoDTO pago) {
        if (pago.getMontoPagado() == null || pago.getMontoPagado() <= 0) {
            throw new IllegalArgumentException("El monto pagado debe ser mayor a 0.");
        }
    }

    // ─── Verificación HTTP entre microservicios ───────────────────────────────

    /**
     * REGLA: El pago debe estar asociado a un presupuesto existente
     *        (que a su vez ya tiene vinculada la prestación).
     */
    private void verificarPresupuestoExiste(Integer presupuestoId) {
        if (presupuestoId == null) {
            throw new IllegalArgumentException(
                "Regla de negocio: el pago debe estar asociado a una prestación " +
                "(via presupuesto). Indique el presupuestoId.");
        }
        Boolean existe = webClientPresupuesto.get()
            .uri("/presupuesto/{id}", presupuestoId)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,
                resp -> Mono.error(new IllegalArgumentException("Presupuesto no encontrado")))
            .onStatus(HttpStatusCode::is5xxServerError,
                resp -> Mono.error(new RuntimeException("Error en servicio de presupuesto")))
            .toBodilessEntity()
            .map(r -> true)
            .onErrorReturn(IllegalArgumentException.class, false)
            .block();

        if (Boolean.FALSE.equals(existe)) {
            throw new IllegalArgumentException(
                "Regla de negocio: no existe el presupuesto con ID: " + presupuestoId +
                ". Cree el presupuesto antes de registrar el pago.");
        }
    }

    // ─── CRUD ────────────────────────────────────────────────────────────────

    public List<PagoModel> buscarTodos() {
        return pagoRepository.findAll();
    }

    public Optional<PagoModel> buscarPorId(Integer id) {
        return pagoRepository.findById(id);
    }

    public List<PagoModel> buscarPorPaciente(Integer pacienteId) {
        return pagoRepository.findByPacienteId(pacienteId);
    }

    public List<PagoModel> buscarPorEstado(String estado) {
        return pagoRepository.findByEstadoPago(estado);
    }

    public List<PagoModel> buscarPorPresupuesto(Integer presupuestoId) {
        return pagoRepository.findByPresupuestoId(presupuestoId);
    }

    public Boolean registrarPago(PagoDTO pago) {
        if (pago == null) {
            throw new IllegalArgumentException("Los datos del pago no pueden ser nulos.");
        }

        // 1. Verificar que el presupuesto (y su prestación asociada) existen
        verificarPresupuestoExiste(pago.getPresupuestoId());

        // 2. Validar pacienteId presente
        if (pago.getPacienteId() == null) {
            throw new IllegalArgumentException(
                "Regla de negocio: el pago debe estar asociado a un paciente. Indique el pacienteId.");
        }

        // 3. Validaciones de datos
        validarRut(pago.getRutPaciente());
        validarMontos(pago);

        PagoModel pagoModel = new PagoModel();
        pagoModel.setPacienteId(pago.getPacienteId());
        pagoModel.setNombrePaciente(pago.getNombrePaciente());
        pagoModel.setRutPaciente(pago.getRutPaciente().trim());
        pagoModel.setPresupuestoId(pago.getPresupuestoId());
        pagoModel.setMontoPagado(pago.getMontoPagado());
        pagoModel.setMontoTotal(pago.getMontoTotal());
        pagoModel.setFechaPago(pago.getFechaPago());
        pagoModel.setMetodoPago(pago.getMetodoPago());
        pagoModel.setEstadoPago(pago.getEstadoPago());

        pagoRepository.save(pagoModel);
        return true;
    }
}
