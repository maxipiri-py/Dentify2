package com.dentify.presupuesto;

import java.util.List;
 
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
 
    public List<PresupuestoModel> buscarTodos() {
        return presupuestoRepository.findAll();
    }
    public List<PresupuestoModel> buscarPorEstado(String estado) {
        return presupuestoRepository.findByEstado(estado);
    }
}