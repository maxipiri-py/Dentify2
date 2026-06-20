package com.dentify.presupuesto.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dentify.presupuesto.Model.PresupuestoModel;
import com.dentify.presupuesto.Service.PresupuestoService;

@RestController
@RequestMapping("/api/presupuesto")
public class PresupuestoController {

    @Autowired
    private PresupuestoService presupuestoService;

    @PostMapping("/crear-presupuesto")
    public ResponseEntity<String> crearPresupuesto(@RequestBody PresupuestoModel presupuesto) {
        try {
            Boolean save = presupuestoService.guardarPresupuesto(presupuesto);
            if (!save) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error: No se pudo crear el presupuesto en la base de datos.");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Presupuesto creado correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        Optional<PresupuestoModel> presupuesto = presupuestoService.buscarPorId(id);
        if (presupuesto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe el presupuesto con ID: " + id);
        }
        return ResponseEntity.ok(presupuesto.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPresupuesto(@PathVariable Integer id) {
        boolean eliminado = presupuestoService.eliminar(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe el presupuesto con ID: " + id);
        }
        return ResponseEntity.ok("Presupuesto eliminado correctamente");
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody PresupuestoModel presupuesto) {
        Optional<PresupuestoModel> actualizado = presupuestoService.actualizar(id, presupuesto);
        if (actualizado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe el presupuesto con ID: " + id);
        }
        return ResponseEntity.ok(actualizado.get());
    }
}
