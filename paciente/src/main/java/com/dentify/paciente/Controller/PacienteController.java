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
    public ResponseEntity<List<PacienteModel>> buscarTodos(){

        List<PacienteModel> listaPacientes =
                pacienteService.buscarTodos();

        if(listaPacientes.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(listaPacientes);
    }

    // CREAR
    @PostMapping
    public ResponseEntity<PacienteModel> crear(
            @RequestBody PacienteDTO paciente){

        return ResponseEntity.ok(
                pacienteService.crear(paciente)
        );
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPaciente(
            @PathVariable Integer id){

        Optional<PacienteModel> paciente =
                pacienteService.buscarPorId(id);

        if(paciente.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(paciente.get());
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPaciente(
            @PathVariable Integer id,
            @RequestBody PacienteDTO dto){

        Optional<PacienteModel> paciente =
                pacienteService.actualizar(id, dto);

        if(paciente.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(paciente.get());
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPaciente(
            @PathVariable Integer id){

        boolean eliminado =
                pacienteService.eliminar(id);

        if(!eliminado){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                "Paciente eliminado correctamente"
        );
    }
}
