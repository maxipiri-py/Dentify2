package com.dentify.paciente.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dentify.paciente.DTO.PacienteDTO;
import com.dentify.paciente.Model.PacienteModel;
import com.dentify.paciente.Repository.PacienteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    // ─────────────────────────────────────────────────────────────
    // VALIDACIONES
    // ─────────────────────────────────────────────────────────────

    private void validarRut(String rut) {

        if (rut == null || rut.isBlank()) {
            throw new IllegalArgumentException(
                    "El RUT no puede estar vacío.");
        }

        String rutLimpio = rut.trim();

        if (rutLimpio.length() > 12) {
            throw new IllegalArgumentException(
                    "El RUT no puede tener más de 12 caracteres (incluyendo el guion). Formato: 12345678-9");
        }

        if (!rutLimpio.matches("^[0-9]{7,8}-[0-9Kk]$")) {
            throw new IllegalArgumentException(
                    "Formato de RUT inválido. Use el formato: 12345678-9 o 1234567-K");
        }
    }

    private void validarCamposObligatorios(PacienteDTO dto) {

        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del paciente no puede estar vacío.");
        }

        if (dto.getApellido() == null || dto.getApellido().isBlank()) {
            throw new IllegalArgumentException(
                    "El apellido del paciente no puede estar vacío.");
        }

        validarRut(dto.getRut());
    }

    // ─────────────────────────────────────────────────────────────
    // LISTAR TODOS
    // ─────────────────────────────────────────────────────────────

    public List<PacienteModel> buscarTodos() {
        return pacienteRepository.findAll();
    }

    public List<PacienteModel> listarTodos() {
        return pacienteRepository.findAll();
    }

    // ─────────────────────────────────────────────────────────────
    // CREAR
    // ─────────────────────────────────────────────────────────────

    public PacienteModel crear(PacienteDTO pacienteDTO) {

        validarCamposObligatorios(pacienteDTO);

        PacienteModel paciente = new PacienteModel();

        paciente.setNombre(
                pacienteDTO.getNombre().trim());

        paciente.setApellido(
                pacienteDTO.getApellido().trim());

        paciente.setRut(
                pacienteDTO.getRut().trim());

        paciente.setFechaNacimiento(
                pacienteDTO.getFechaNacimiento());

        paciente.setCorreo(
                pacienteDTO.getCorreo());

        return pacienteRepository.save(paciente);
    }

    // ─────────────────────────────────────────────────────────────
    // BUSCAR POR ID
    // ─────────────────────────────────────────────────────────────

    public Optional<PacienteModel> buscarPorId(Integer id) {
        return pacienteRepository.findById(id);
    }

    // ─────────────────────────────────────────────────────────────
    // HISTORIAL COMPLETO
    // ─────────────────────────────────────────────────────────────

    public PacienteModel obtenerHistorialCompleto(Integer id) {

        return pacienteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Paciente no encontrado"));
    }

    // ─────────────────────────────────────────────────────────────
    // ACTUALIZAR
    // ─────────────────────────────────────────────────────────────

    public Optional<PacienteModel> actualizar(
            Integer id,
            PacienteDTO dto) {

        Optional<PacienteModel> pacienteOptional =
                pacienteRepository.findById(id);

        if (pacienteOptional.isEmpty()) {
            return Optional.empty();
        }

        validarCamposObligatorios(dto);

        PacienteModel paciente =
                pacienteOptional.get();

        paciente.setNombre(
                dto.getNombre().trim());

        paciente.setApellido(
                dto.getApellido().trim());

        paciente.setRut(
                dto.getRut().trim());

        paciente.setFechaNacimiento(
                dto.getFechaNacimiento());

        paciente.setCorreo(
                dto.getCorreo());

        PacienteModel pacienteActualizado =
                pacienteRepository.save(paciente);

        return Optional.of(pacienteActualizado);
    }

    // ─────────────────────────────────────────────────────────────
    // ELIMINAR
    // ─────────────────────────────────────────────────────────────

    public boolean eliminar(Integer id) {

        Optional<PacienteModel> paciente =
                pacienteRepository.findById(id);

        if (paciente.isEmpty()) {
            return false;
        }

        pacienteRepository.deleteById(id);

        return true;
    }
}