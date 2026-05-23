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

    public Boolean generarReporte(ReporteDTO reporte) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generarReporte'");
    }

    public Optional<ReporteDTO> buscarPorId(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorId'");
    }
}