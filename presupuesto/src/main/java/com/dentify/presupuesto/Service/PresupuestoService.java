package com.dentify.presupuesto.Service;
 
import java.util.List;
import java.util.Optional;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.dentify.presupuesto.Model.PresupuestoModel;
import com.dentify.presupuesto.Repository.PresupuestoRepository;
 
import jakarta.transaction.Transactional;
 
@Service
@Transactional
public class PresupuestoService {
 
    @Autowired
    private PresupuestoRepository presupuestoRepository;
 
    public Optional<PresupuestoModel> buscarPorId(Integer id) {
        return presupuestoRepository.findById(id);
    }

   public List<PresupuestoModel> buscarPorEstado(String estado) {
        return presupuestoRepository.findByEstadoPago(estado);
    }

   public Boolean guardarPresupuesto(PresupuestoModel presupuesto) {
        try {
            presupuestoRepository.save(presupuesto);
            return true;
        } catch (Exception e) {
            return false;
        }
   }
}