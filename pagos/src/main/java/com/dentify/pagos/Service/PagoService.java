package com.dentify.pagos.Service;
 
import java.util.List;
import java.util.Optional;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dentify.pagos.DTO.PagoDTO;
import com.dentify.pagos.Model.PagoModel;
import com.dentify.pagos.Repository.PagoRepository;
 
import jakarta.transaction.Transactional;
 
@Service
@Transactional
public class PagoService {
 
    @Autowired
    private PagoRepository pagoRepository;

 
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
        return pagoRepository.findByEstado(estado);
    }
 
    public List<PagoModel> buscarPorPresupuesto(Integer presupuestoId) {
        return pagoRepository.findByPresupuestoId(presupuestoId);
    }

    public Boolean registrarPago(PagoDTO pago) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registrarPago'");
    }
 
   
}
 