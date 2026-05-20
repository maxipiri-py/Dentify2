package com.dentify.dentista.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dentify.dentista.Model.Dentista;
import com.dentify.dentista.Service.DentistaService;

@RestController
@RequestMapping("/api/dentistas")
public class DentistaController {
    @Autowired
    private DentistaService dentistaService;

    @PostMapping
    public ResponseEntity<String> crearDentista() {
        // Lógica para crear un dentista
        return ResponseEntity.ok("Dentista agregado exitosamente");
    }

    @GetMapping("({id})")
    public ResponseEntity<?>buscarPorId(@PathVariable Integer id) {
        Optional<Dentista> dentista = dentistaService.buscarPorId(id);
        if (dentista.isEmpty()){
       //     return ResponseEntity.status(HttpStatus.NOT_FOUND)
        //        .body("Dentista con id " + id + " no encontrado");
        }
        return ResponseEntity.ok(dentista.get());
        }


}
