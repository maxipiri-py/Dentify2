package com.dentify.notificaciones.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dentify.notificaciones.DTO.NotificacionDTO;
import com.dentify.notificaciones.Model.Notificacion;
import com.dentify.notificaciones.Repository.NotificacionRepository;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    public Notificacion enviar(NotificacionDTO dto){

        Notificacion notificacion = new Notificacion();

        notificacion.setCorreo(dto.getCorreo());
        notificacion.setAsunto(dto.getAsunto());
        notificacion.setMensaje(dto.getMensaje());

        // SIMULACION
        notificacion.setEstado("ENVIADO");

        System.out.println("Correo enviado a: " + dto.getCorreo());
        System.out.println("Mensaje: " + dto.getMensaje());

        return notificacionRepository.save(notificacion);
    }

    public List<Notificacion> listar(){

        return notificacionRepository.findAll();
    }
}
