package com.dentify.notificaciones.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dentify.notificaciones.Model.Notificacion;

@Repository
public interface NotificacionRepository
        extends JpaRepository<Notificacion, Integer> {

}
