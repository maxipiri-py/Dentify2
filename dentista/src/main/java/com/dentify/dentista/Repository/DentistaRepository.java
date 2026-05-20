package com.dentify.dentista.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dentify.dentista.Model.Dentista;

@Repository
public interface DentistaRepository extends JpaRepository<Dentista, Integer> {

}
