package com.dentify.reporte.Service;
 
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dentify.reporte.DTO.ReporteDTO;
import com.dentify.reporte.Model.ReporteModel;
import com.dentify.reporte.Repository.ReporteRepository;
 
import jakarta.transaction.Transactional;
 
@Service
@Transactional
public class ReporteService {
 
    @Autowired
    private ReporteRepository reporteRepository;
    
    public List<ReporteModel> buscarTodos() {
        return reporteRepository.findAll();
    }

    public Boolean generarReporte(ReporteDTO dto) {
        try {
            ReporteModel model = convertToModel(dto);
            reporteRepository.save(model);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<ReporteDTO> buscarPorId(Integer id) {
        return reporteRepository.findById(id)
                .map(this::convertToDTO);
    }

    private ReporteModel convertToModel(ReporteDTO dto) {
        if (dto == null) return null;
        ReporteModel model = new ReporteModel();
        model.setId(dto.getId());
        model.setTipoReporte(dto.getTipoReporte());
        model.setDescripcion(dto.getDescripcion());
        model.setFechaGeneracion(dto.getFechaGeneracion());
        model.setTotalPacientes(dto.getTotalPacientes());
        model.setTotalCitas(dto.getTotalCitas());
        model.setTotalIngresos(dto.getTotalIngresos());
        return model;
    }

    private ReporteDTO convertToDTO(ReporteModel model) {
        if (model == null) return null;
        ReporteDTO dto = new ReporteDTO();
        dto.setId(model.getId());
        dto.setTipoReporte(model.getTipoReporte());
        dto.setDescripcion(model.getDescripcion());
        dto.setFechaGeneracion(model.getFechaGeneracion());
        dto.setTotalPacientes(model.getTotalPacientes());
        dto.setTotalCitas(model.getTotalCitas());
        dto.setTotalIngresos(model.getTotalIngresos());
        return dto;
    }
}