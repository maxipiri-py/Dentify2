package com.dentify.pagos.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dentify.pagos.DTO.PagoDTO;
import com.dentify.pagos.Model.PagoModel;
import com.dentify.pagos.Service.PagoService;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @PostMapping
    public ResponseEntity<String> registrarPago(@RequestBody PagoDTO pago) {
        try {
            pagoService.registrarPago(pago);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Pago registrado correctamente.");

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: No se pudo registrar el pago en la base de datos.");
        }
    }

    @GetMapping("/lista-pagos")
    public ResponseEntity<List<PagoModel>> listarTodos() {

        return ResponseEntity.ok(
                pagoService.buscarTodos());
    }

    @GetMapping("/estado-pago")
    public ResponseEntity<List<PagoModel>> buscarPorEstado(
            @RequestParam String estado) {

        return ResponseEntity.ok(
                pagoService.buscarPorEstado(estado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPago(
            @PathVariable Integer id,
            @RequestBody PagoDTO pagoDTO) {

        try {

            var pagoActualizado =
                    pagoService.actualizarPago(id, pagoDTO);

            if (pagoActualizado.isEmpty()) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No existe el pago con ID: " + id);
            }

            return ResponseEntity.ok(
                    pagoActualizado.get());

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPago(
            @PathVariable Integer id) {

        boolean eliminado =
                pagoService.eliminarPago(id);

        if (!eliminado) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No existe el pago con ID: " + id);
        }

        return ResponseEntity.ok(
                "Pago eliminado correctamente");
    }
}