package com.dentify.observaciones.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dentify.observaciones.DTO.ObservacionDTO;
import com.dentify.observaciones.Model.Observacion;
import com.dentify.observaciones.Service.ObservacionService;

@RestController
@RequestMapping("/api/observaciones")
public class ObservacionController {

    @Autowired
    private ObservacionService observacionService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ObservacionDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(observacionService.crear(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Error de comunicación con otro servicio: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Observacion>> listar() {
        return ResponseEntity.ok(observacionService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        Optional<Observacion> observacion = observacionService.buscarPorId(id);
        if (observacion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe la observación con ID: " + id);
        }
        return ResponseEntity.ok(observacion.get());
    }

    @GetMapping("/agenda/{agendaId}")
    public ResponseEntity<List<Observacion>> buscarPorAgenda(@PathVariable Integer agendaId) {
        return ResponseEntity.ok(observacionService.buscarPorAgenda(agendaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody ObservacionDTO dto) {
        try {
            Optional<Observacion> observacion = observacionService.actualizar(id, dto);
            if (observacion.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No existe la observación con ID: " + id);
            }
            return ResponseEntity.ok(observacion.get());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        boolean eliminado = observacionService.eliminar(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe la observación con ID: " + id);
        }
        return ResponseEntity.ok("Observación eliminada correctamente");
    }
}
