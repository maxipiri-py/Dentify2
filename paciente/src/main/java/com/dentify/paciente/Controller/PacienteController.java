package com.dentify.paciente.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dentify.paciente.DTO.PacienteDTO;
import com.dentify.paciente.Model.PacienteModel;
import com.dentify.paciente.Service.PacienteService;

@RestController
@RequestMapping("/api/paciente")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<List<PacienteModel>> buscarTodos() {
        List<PacienteModel> listaPacientes = pacienteService.buscarTodos();
        if (listaPacientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listaPacientes);
    }

    @PostMapping
    public ResponseEntity<?> crearPaciente(@RequestBody PacienteDTO paciente) {
        try {
            PacienteModel nuevo = pacienteService.crear(paciente);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPaciente(@PathVariable Integer id) {
        try {
            PacienteModel paciente = pacienteService.obtenerHistorialCompleto(id);
            return ResponseEntity.ok(paciente);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
