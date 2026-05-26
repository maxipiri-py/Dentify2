package com.dentify.pagos.Controller;
  
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
 
import com.dentify.pagos.DTO.PagoDTO;
import com.dentify.pagos.Model.PagoModel;
import com.dentify.pagos.Service.PagoService;
 
@RestController
@RequestMapping("/api/pagos")
public class PagoController {
 
    @Autowired
    private PagoService pagoService;
 
    // CORRECCIÓN: Quitamos el "/registrarPago" para seguir el estándar REST
    @PostMapping 
    public ResponseEntity<String> registrarPago(@RequestBody PagoDTO pago) {
        Boolean registrado = pagoService.registrarPago(pago);
        if (!registrado) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: No se pudo registrar el pago.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Pago registrado correctamente.");
    }
    
    @GetMapping("/lista-pagos")
    public ResponseEntity<List<PagoModel>> listarTodos() {
        return ResponseEntity.ok(pagoService.buscarTodos());
    }

    @GetMapping("/estado-pago")
    public ResponseEntity<List<PagoModel>> buscarPorEstado(@RequestParam String estado) {
        return ResponseEntity.ok(pagoService.buscarPorEstado(estado));
    }
}