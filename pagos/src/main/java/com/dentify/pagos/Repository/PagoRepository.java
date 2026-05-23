package com.dentify.pagos.Repository;
 
import java.util.List;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import com.dentify.pagos.Model.PagoModel;
 
@Repository
public interface PagoRepository extends JpaRepository<PagoModel, Integer> {
 
    List<PagoModel> findByPacienteId(Integer pacienteId);
 
    List<PagoModel> findByEstado(String estado);
 
    List<PagoModel> findByPresupuestoId(Integer presupuestoId);
}
 