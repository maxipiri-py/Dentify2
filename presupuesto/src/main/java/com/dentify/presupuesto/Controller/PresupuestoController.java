package com.dentify.presupuesto.Controller;
 
import java.util.List;
import java.util.Optional;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
 
import com.dentify.presupuesto.Model.PresupuestoModel;
import com.dentify.presupuesto.Service.PresupuestoService;
 
@RestController
@RequestMapping("/api/presupuesto")
public class PresupuestoController {
 
    @Autowired
    private PresupuestoService presupuestoService;
    
    @PostMapping("/crear-persupuesto")
    public ResponseEntity<String> crearPresupuesto(@RequestBody PresupuestoModel presupuesto) {
        Boolean save = presupuestoService.guardarPresupuesto(presupuesto);
        if (save!=true) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se pudo crear el presupuesto.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Presupuesto creado correctamente.");
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

       @GetMapping("/estado")
    public ResponseEntity<List<PresupuestoModel>> buscarPorEstado(@RequestParam String estado) {
        return ResponseEntity.ok(presupuestoService.buscarPorEstado(estado));
    }

}
 