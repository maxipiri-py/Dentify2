package com.dentify.observaciones.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Observacion> crear(
            @RequestBody ObservacionDTO dto){

        return ResponseEntity.ok(
                observacionService.crear(dto)
        );
    }

    @GetMapping
    public ResponseEntity<List<Observacion>> listar(){

        return ResponseEntity.ok(
                observacionService.listar()
        );
    }
}
