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

    // ─── Validaciones ────────────────────────────────────────────────────────
    private void validarRut(String rut) {
        if (rut == null || rut.isBlank()) {
            throw new IllegalArgumentException("El RUT no puede estar vacío.");
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

    private void validarCamposObligatorios(DentistaDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del dentista no puede estar vacío.");
        }
        if (dto.getApellido() == null || dto.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido del dentista no puede estar vacío.");
        }
        validarRut(dto.getRut());
    }

    public Dentista crear(DentistaDTO dentistaDTO) {
        validarCamposObligatorios(dentistaDTO);

        Dentista dentista = new Dentista();
        dentista.setRut(dentistaDTO.getRut().trim());
        dentista.setNombre(dentistaDTO.getNombre().trim());
        dentista.setApellido(dentistaDTO.getApellido().trim());
        dentista.setEspecialidad(dentistaDTO.getEspecialidad());
        dentista.setCorreo(dentistaDTO.getCorreo());
        dentista.setTelefono(dentistaDTO.getTelefono());
        return dentistaRepository.save(dentista);
    }

    public Optional<Dentista> buscarPorId(Integer id) {
        return dentistaRepository.findById(id);
    }

    public List<Dentista> listarTodos() {
        return dentistaRepository.findAll();
    }

    public List<Dentista> buscarTodos() {
        return dentistaRepository.findAll();
    }

    public Optional<Dentista> actualizar(Integer id, DentistaDTO dentistaDTO) {
        Optional<Dentista> dentistaOptional = dentistaRepository.findById(id);
        if (dentistaOptional.isEmpty()) {
            return Optional.empty();
        }

        validarCamposObligatorios(dentistaDTO);

        Dentista dentista = dentistaOptional.get();
        dentista.setRut(dentistaDTO.getRut().trim());
        dentista.setNombre(dentistaDTO.getNombre().trim());
        dentista.setApellido(dentistaDTO.getApellido().trim());
        dentista.setEspecialidad(dentistaDTO.getEspecialidad());
        dentista.setCorreo(dentistaDTO.getCorreo());
        dentista.setTelefono(dentistaDTO.getTelefono());

        return Optional.of(dentistaRepository.save(dentista));
    }

    public boolean eliminar(Integer id) {
        Optional<Dentista> dentista = dentistaRepository.findById(id);
        if (dentista.isEmpty()) {
            return false;
        }
        dentistaRepository.deleteById(id);
        return true;
    }
}
