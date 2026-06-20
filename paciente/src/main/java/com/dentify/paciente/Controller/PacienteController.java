package com.dentify.paciente.Controller;

import java.util.List;
import java.util.Optional;

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

    // LISTAR TODOS
    @GetMapping
    public ResponseEntity<List<PacienteModel>> buscarTodos() {

        List<PacienteModel> listaPacientes =
                pacienteService.buscarTodos();

        if (listaPacientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(listaPacientes);
    }

    // CREAR
    @PostMapping
    public ResponseEntity<?> crear(
            @RequestBody PacienteDTO pacienteDTO) {

        try {
            PacienteModel pacienteCreado =
                    pacienteService.crear(pacienteDTO);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(pacienteCreado);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPaciente(
            @PathVariable Integer id) {

        Optional<PacienteModel> paciente =
                pacienteService.buscarPorId(id);

        if (paciente.isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No existe el paciente con ID: " + id);
        }

        return ResponseEntity.ok(paciente.get());
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPaciente(
            @PathVariable Integer id,
            @RequestBody PacienteDTO dto) {

        try {

            Optional<PacienteModel> paciente =
                    pacienteService.actualizar(id, dto);

            if (paciente.isEmpty()) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No existe el paciente con ID: " + id);
            }

            return ResponseEntity.ok(paciente.get());

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPaciente(
            @PathVariable Integer id) {

        boolean eliminado =
                pacienteService.eliminar(id);

        if (!eliminado) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No existe el paciente con ID: " + id);
        }

        return ResponseEntity.ok(
                "Paciente eliminado correctamente");
    }
}