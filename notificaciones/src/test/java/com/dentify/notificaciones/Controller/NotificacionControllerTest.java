package com.dentify.notificaciones.Controller;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.dentify.notificaciones.  DTO.NotificacionDTO;
import com.dentify.notificaciones.Model.Notificacion;
import com.dentify.notificaciones.Service.NotificacionService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(NotificacionController.class)

public class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificacionService notificacionService;

    @Test
    @DisplayName("POST /api/notificaciones -> envia la notificacion correctamente")
    public void testEnviarNotificacion() throws Exception {
        var notificacion = new Notificacion();
        notificacion.setId(1);
        notificacion.setCorreo("paciente@gmail.com");
        notificacion.setAsunto("Recordatorio de cita");
        notificacion.setMensaje("Tu cita es mañana a las 10:00");

        when(notificacionService.enviar(any(NotificacionDTO.class))).thenReturn(notificacion);

        String json = """
                {
                    "correo": "paciente@gmail.com",
                    "asunto": "Recordatorio de cita",
                    "mensaje": "Tu cita es mañana a las 10:00"
                }
                """;

        mockMvc.perform(post("/api/notificaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.correo").value("paciente@gmail.com"));
    }

    @Test
    @DisplayName("POST /api/notificaciones -> retorna error cuando el servicio falla")
    public void testEnviarNotificacionFalla() throws Exception {
        when(notificacionService.enviar(any(NotificacionDTO.class))).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/api/notificaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isInternalServerError());
    }   


    @Test
    @DisplayName("GET /api/notificaciones -> retorna el listado completo")
    public void testListarNotificaciones() throws Exception {
        var notificacion = new Notificacion();
        notificacion.setId(1);
        notificacion.setCorreo("paciente@gmail.com");
        notificacion.setAsunto("Recordatorio de cita");

        when(notificacionService.listar()).thenReturn(List.of(notificacion));

        mockMvc.perform(get("/api/notificaciones")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].asunto").value("Recordatorio de cita"));
    }

    @Test
    @DisplayName("GET /api/notificaciones -> retorna lista vacía")
    public void testListarNotificacionesVacio() throws Exception {
        when(notificacionService.listar()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/notificaciones")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
