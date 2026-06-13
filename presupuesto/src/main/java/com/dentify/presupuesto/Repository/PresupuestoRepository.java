package com.dentify.presupuesto.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dentify.presupuesto.Model.PresupuestoModel;

@Repository
public interface PresupuestoRepository extends JpaRepository<PresupuestoModel, Integer> {

    List<PresupuestoModel> findByPacienteId(Integer pacienteId);

    List<PresupuestoModel> findByEstadoPago(String estadoPago);

    List<PresupuestoModel> findByDentistaId(Integer dentistaId);

    List<PresupuestoModel> findByPrestacionId(Integer prestacionId);
}
