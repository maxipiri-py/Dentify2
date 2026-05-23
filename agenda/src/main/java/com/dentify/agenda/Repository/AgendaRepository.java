package com.dentify.agenda.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dentify.agenda.DTO.AgendaDTO;

@Repository
public interface AgendaRepository extends JpaRepository<AgendaDTO, Integer>{

}
