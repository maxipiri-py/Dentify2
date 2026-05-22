package com.dentify.dentista.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

    // Crear un nuevo dentista
    @PostMapping
    public ResponseEntity<Dentista> crearDentista(@RequestBody DentistaDTO dentistaDTO) {
        Dentista nuevoDentista = dentistaService.crear(dentistaDTO);
        return ResponseEntity.ok(nuevoDentista);
    }

    // Buscar un dentista por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarDentista(@PathVariable Integer id) {
        Optional<Dentista> dentista = dentistaService.buscarPorId(id);
        if (dentista.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dentista.get());
    }

    // Listar todos los dentistas
    @GetMapping
    public ResponseEntity<List<Dentista>> listarDentistas() {
        List<Dentista> lista = dentistaService.listarTodos();
        return ResponseEntity.ok(lista);
    }
}