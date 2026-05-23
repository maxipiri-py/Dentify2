package com.dentify.reporte.Repository;
 
import java.time.LocalDate;
import java.util.List;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import com.dentify.reporte.Model.ReporteModel;
 
@Repository
public interface ReporteRepository extends JpaRepository<ReporteModel, Integer> {
 
    List<ReporteModel> findByTipoReporte(String tipoReporte); 
}
 