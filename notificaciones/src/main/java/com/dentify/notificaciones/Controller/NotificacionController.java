package com.dentify.notificaciones.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Notificacion> enviar(
            @RequestBody NotificacionDTO dto){

        return ResponseEntity.ok(
                notificacionService.enviar(dto)
        );
    }

    @GetMapping
    public ResponseEntity<List<Notificacion>> listar(){

        return ResponseEntity.ok(
                notificacionService.listar()
        );
    }
}
