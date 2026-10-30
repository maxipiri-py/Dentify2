package com.dentify.notificaciones.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dentify.notificaciones.DTO.NotificacionDTO;
import com.dentify.notificaciones.Model.Notificacion;
import com.dentify.notificaciones.Repository.NotificacionRepository;

@Service
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    @Autowired
    private NotificacionRepository notificacionRepository;

    public Notificacion enviar(NotificacionDTO dto){

        Notificacion notificacion = new Notificacion();

        notificacion.setCorreo(dto.getCorreo());
        notificacion.setAsunto(dto.getAsunto());
        notificacion.setMensaje(dto.getMensaje());

        // SIMULACION
        notificacion.setEstado("ENVIADO");

        log.info("Correo enviado a: {}", dto.getCorreo());
        log.debug("Mensaje: {}", dto.getMensaje());

        return notificacionRepository.save(notificacion);
    }

    public List<Notificacion> listar(){

        return notificacionRepository.findAll();
    }
}
