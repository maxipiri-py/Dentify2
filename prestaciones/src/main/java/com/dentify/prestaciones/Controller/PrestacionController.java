package com.dentify.prestaciones.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dentify.prestaciones.DTO.PrestacionDTO;
import com.dentify.prestaciones.Model.Prestacion;
import com.dentify.prestaciones.Service.PrestacionService;

@RestController
@RequestMapping("/api/prestaciones")
public class PrestacionController {
    @Autowired
    private PrestacionService prestacionService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody PrestacionDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(prestacionService.crear(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Prestacion>> listar() {
        return ResponseEntity.ok(prestacionService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        Optional<Prestacion> prestacion = prestacionService.buscarPorId(id);
        if (prestacion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe la prestación con ID: " + id);
        }
        return ResponseEntity.ok(prestacion.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody PrestacionDTO dto) {
        try {
            Optional<Prestacion> prestacion = prestacionService.actualizar(id, dto);
            if (prestacion.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No existe la prestación con ID: " + id);
            }
            return ResponseEntity.ok(prestacion.get());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        boolean eliminado = prestacionService.eliminar(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe la prestación con ID: " + id);
        }
        return ResponseEntity.ok("Prestación eliminada correctamente");
    }
}
