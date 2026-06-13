package com.dentify.dentista.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dentify.dentista.DTO.DentistaDTO;
import com.dentify.dentista.Model.Dentista;
import com.dentify.dentista.Service.DentistaService;

@RestController
@RequestMapping("/api/dentistas")
public class DentistaController {

    @Autowired
    private DentistaService dentistaService;

    @PostMapping
    public ResponseEntity<?> crearDentista(@RequestBody DentistaDTO dentistaDTO) {
        try {
            Dentista nuevoDentista = dentistaService.crear(dentistaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDentista);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarDentista(@PathVariable Integer id) {
        Optional<Dentista> dentista = dentistaService.buscarPorId(id);
        if (dentista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe el dentista con ID: " + id);
        }
        return ResponseEntity.ok(dentista.get());
    }

    @GetMapping
    public ResponseEntity<List<Dentista>> listarDentistas() {
        return ResponseEntity.ok(dentistaService.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarDentista(
            @PathVariable Integer id,
            @RequestBody DentistaDTO dentistaDTO) {
        try {
            Optional<Dentista> dentistaActualizado = dentistaService.actualizar(id, dentistaDTO);
            if (dentistaActualizado.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No existe el dentista con ID: " + id);
            }
            return ResponseEntity.ok(dentistaActualizado.get());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDentista(@PathVariable Integer id) {
        boolean eliminado = dentistaService.eliminar(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe el dentista con ID: " + id);
        }
        return ResponseEntity.ok("Dentista eliminado correctamente");
    }
}
