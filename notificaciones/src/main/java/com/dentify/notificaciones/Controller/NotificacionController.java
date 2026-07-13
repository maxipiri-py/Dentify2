package com.dentify.notificaciones.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dentify.notificaciones.DTO.NotificacionDTO;
import com.dentify.notificaciones.Model.Notificacion;
import com.dentify.notificaciones.Service.NotificacionService;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @PostMapping
    public ResponseEntity<?> enviar(@RequestBody NotificacionDTO dto) {
        try {
            return ResponseEntity.ok(notificacionService.enviar(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: No se pudo enviar la notificación.");
        }
    }

    @GetMapping
    public ResponseEntity<List<Notificacion>> listar() {
        return ResponseEntity.ok(notificacionService.listar());
    }
}