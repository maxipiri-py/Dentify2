package com.dentify.reporte.Controller;
 
import java.util.Optional;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import com.dentify.reporte.DTO.ReporteDTO;
import com.dentify.reporte.Service.ReporteService;
 
@RestController
@RequestMapping("/api/reporte")
public class ReporteController {
 
    @Autowired
    private ReporteService reporteService;

      @PostMapping("/generar-reporte")
    public ResponseEntity<String> generarReporte(@RequestBody ReporteDTO reporte) {
        Boolean reportegenerado = reporteService.generarReporte(reporte);
        return ResponseEntity.status(HttpStatus.CREATED).body("Reporte generado correctamente.");
    }

      @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        Optional<ReporteDTO> reporte = reporteService.buscarPorId(id);
        if (reporte.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe el reporte con ID: " + id);
        }
        return ResponseEntity.ok(reporte.get());
    }
}