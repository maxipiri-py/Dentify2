package com.dentify.prestaciones.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dentify.prestaciones.DTO.PrestacionDTO;
import com.dentify.prestaciones.Model.Prestacion;
import com.dentify.prestaciones.Repository.PrestacionRepository;

@Service
public class PrestacionService {

    @Autowired
    private PrestacionRepository prestacionRepository;

    // ─── Validaciones ────────────────────────────────────────────────────────
    private void validarCamposObligatorios(PrestacionDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la prestación no puede estar vacío.");
        }
        if (dto.getValor() == null || dto.getValor() <= 0) {
            throw new IllegalArgumentException("El valor de la prestación debe ser mayor a 0.");
        }
    }

    public Prestacion crear(PrestacionDTO dto) {
        validarCamposObligatorios(dto);

        Prestacion prestacion = new Prestacion();
        prestacion.setNombre(dto.getNombre().trim());
        prestacion.setDescripcion(dto.getDescripcion());
        prestacion.setValor(dto.getValor());

        return prestacionRepository.save(prestacion);
    }

    public List<Prestacion> listar() {
        return prestacionRepository.findAll();
    }

    public Optional<Prestacion> buscarPorId(Integer id) {
        return prestacionRepository.findById(id);
    }

    public Optional<Prestacion> actualizar(Integer id, PrestacionDTO dto) {
        Optional<Prestacion> prestacionOptional = prestacionRepository.findById(id);
        if (prestacionOptional.isEmpty()) {
            return Optional.empty();
        }

        validarCamposObligatorios(dto);

        Prestacion prestacion = prestacionOptional.get();
        prestacion.setNombre(dto.getNombre().trim());
        prestacion.setDescripcion(dto.getDescripcion());
        prestacion.setValor(dto.getValor());

        return Optional.of(prestacionRepository.save(prestacion));
    }

    public boolean eliminar(Integer id) {
        Optional<Prestacion> prestacion = prestacionRepository.findById(id);
        if (prestacion.isEmpty()) {
            return false;
        }
        prestacionRepository.deleteById(id);
        return true;
    }
}
