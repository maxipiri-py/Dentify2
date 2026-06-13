package com.dentify.paciente.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dentify.paciente.Model.PacienteModel;

@Repository
public interface PacienteRepository extends JpaRepository<PacienteModel, Integer> {
    List<PacienteModel> findByApellido(String apellido);
    List<PacienteModel> findByNombre(String nombre);
}
