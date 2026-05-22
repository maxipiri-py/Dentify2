package com.dentify.prestaciones.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Prestacion> crear(
            @RequestBody PrestacionDTO dto){

        return ResponseEntity.ok(
                prestacionService.crear(dto)
        );
    }

    @GetMapping
    public ResponseEntity<List<Prestacion>> listar(){

        return ResponseEntity.ok(
                prestacionService.listar()
        );
    }
}
