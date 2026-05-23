package com.dentify.paciente.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dentify.paciente.Model.PacienteModel;

@Repository
public interface PacienteRepository extends JpaRepository<PacienteModel, Integer> {
    List<PacienteModel> findByApellido(String apellido);
    @Query(value="Select p from paciente p nombre = :nombre", nativeQuery = true)
    List<PacienteModel> buscarPorNombre(String nombre);

}
