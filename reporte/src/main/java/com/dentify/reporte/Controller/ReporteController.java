package com.dentify.reporte.Controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dentify.reporte.DTO.ReporteDTO;
import com.dentify.reporte.Service.ReporteService;

@RestController
@RequestMapping("/api/reporte")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @PostMapping("/generar-reporte")
    public ResponseEntity<String> generarReporte(@RequestBody ReporteDTO reporte) {
        Boolean reporteGenerado = reporteService.generarReporte(reporte);
        
        // ¡Aquí está la corrección! Ahora evaluamos si realmente se guardó.
        if (reporteGenerado) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Reporte generado correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al generar el reporte. Revisa la consola para más detalles.");
        }
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