package com.dentify.paciente.Controller;

import java.sql.Date;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dentify.paciente.DTO.PacienteDTO;
import com.dentify.paciente.Model.PacienteModel;
import com.dentify.paciente.Service.PacienteService;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping("/api/paciente")
public class PacienteController {
    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<List<PacienteModel>> buscarTodos(){
        List<PacienteModel> listaPacientes = pacienteService.buscarTodos();
        if(listaPacientes.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listaPacientes);
    }
    @PostMapping
    public PacienteModel postMethodName(@RequestBody PacienteDTO paciente){
        return pacienteService.crear(paciente);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PacienteModel> buscarPaciente(@PathVariable Integer id){
        PacienteModel pacientes = pacienteService.obtenerHistorialCompleto(id);
    // Si el objeto es null o no tiene ID, devolvemos 404
        if (pacientes == null || pacientes.getId() == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pacientes);

    }




}
