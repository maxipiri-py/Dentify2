package com.dentify.paciente.Service;

import java.util.List;

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

    // ─── Validación de RUT ────────────────────────────────────────────
    // Acepta formatos: 12345678-9 | 1234567-K  (con guion, máximo 12 caracteres)
    private void validarRut(String rut) {
        if (rut == null || rut.isBlank()) {
            throw new IllegalArgumentException("El RUT no puede estar vacío.");
        }
        String rutLimpio = rut.trim();
        if (rutLimpio.length() > 12) {
            throw new IllegalArgumentException(
                "El RUT no puede tener más de 12 caracteres (incluyendo el guion). Formato: 12345678-9");
        }
        // Debe contener exactamente un guion y tener al menos 3 caracteres
        if (!rutLimpio.matches("^[0-9]{7,8}-[0-9Kk]$")) {
            throw new IllegalArgumentException(
                "Formato de RUT inválido. Use el formato: 12345678-9 o 1234567-K");
        }
    }

    private void validarCamposObligatorios(PacienteDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del paciente no puede estar vacío.");
        }
        if (dto.getApellido() == null || dto.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido del paciente no puede estar vacío.");
        }
        validarRut(dto.getRut());
    }

    public List<PacienteModel> buscarTodos() {
        return pacienteRepository.findAll();
    }

    public PacienteModel crear(PacienteDTO pacienteDTO) {
        validarCamposObligatorios(pacienteDTO);

        PacienteModel paciente = new PacienteModel();
        paciente.setApellido(pacienteDTO.getApellido().trim());
        paciente.setNombre(pacienteDTO.getNombre().trim());
        paciente.setRut(pacienteDTO.getRut().trim());
        paciente.setFechaNacimiento(pacienteDTO.getFechaNacimiento());
        paciente.setCorreo(pacienteDTO.getCorreo());

        return pacienteRepository.save(paciente);
    }

    public PacienteModel obtenerHistorialCompleto(Integer id) {
        return pacienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));
    }

    public List<PacienteModel> listarTodos() {
        return pacienteRepository.findAll();
    }
}
