package com.dentify.agenda.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dentify.agenda.DTO.AgendaDTO;
import com.dentify.agenda.Service.AgendaService;

@RestController
@RequestMapping("/api/agenda")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    @PostMapping("/crear-agenda")
    public ResponseEntity<String> crearAgenda(@RequestBody AgendaDTO agenda) {
        try {
            Boolean save = agendaService.guardarAgenda(agenda);
            if (!save) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error: No se pudo guardar la agenda en la base de datos.");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Agenda creada correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<AgendaDTO>> listarTodos() {
        List<AgendaDTO> lista = agendaService.buscarTodos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable int id) {
        Optional<AgendaDTO> agenda = agendaService.buscarPorId(id);
        if (agenda.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe la agenda con ID: " + id);
        }
        return ResponseEntity.ok(agenda.get());
    }
}
