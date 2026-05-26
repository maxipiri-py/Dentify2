package com.dentify.agenda.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dentify.agenda.Model.AgendaModel;

@Repository
public interface AgendaRepository extends JpaRepository<AgendaModel, Integer>{

}
