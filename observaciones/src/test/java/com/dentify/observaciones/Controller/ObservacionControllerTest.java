package com.dentify.observaciones.Controller;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.dentify.observaciones.DTO.ObservacionDTO;
import com.dentify.observaciones.Model.Observacion;
import com.dentify.observaciones.Service.ObservacionService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(ObservacionController.class)
public class ObservacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ObservacionService observacionService;

    @Test
    @DisplayName("POST /api/observaciones -> crea una observacion nueva")
    public void testCrear() throws Exception {
        var observacion = new Observacion();
        observacion.setId(1);
        observacion.setAgendaId(5);
        observacion.setDescripcion("Paciente con sensibilidad dental");

        when(observacionService.crear(any(ObservacionDTO.class))).thenReturn(observacion);

        String json = """
                {
                    "agendaId": 5,
                    "descripcion": "Paciente con sensibilidad dental"
                }
                """;

        mockMvc.perform(post("/api/observaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.agendaId").value(5));
    }

    @Test
    @DisplayName("POST /api/observaciones -> retorna 403 cuando falla la comunicacion con agenda")
    public void testCrearServicioNoDisponible() throws Exception {
        when(observacionService.crear(any(ObservacionDTO.class)))
                .thenThrow(new RuntimeException("agenda no responde"));

        String json = """
                {
                    "agendaId": 5,
                    "descripcion": "Paciente con sensibilidad dental"
                }
                """;

        mockMvc.perform(post("/api/observaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().string("Error de comunicación con otro servicio: agenda no responde"));
    }

    @Test
    @DisplayName("GET /api/observaciones -> retorna el listado completo")
    public void testListar() throws Exception {
        var observacion = new Observacion();
        observacion.setId(1);
        observacion.setDescripcion("Paciente con sensibilidad dental");

        when(observacionService.listar()).thenReturn(List.of(observacion));

        mockMvc.perform(get("/api/observaciones")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("GET /api/observaciones/{id} -> retorna 404 cuando no existe")
    public void testBuscarPorIdNoExiste() throws Exception {
        when(observacionService.buscarPorId(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/observaciones/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("No existe la observación con ID: 99"));
    }

    @Test
    @DisplayName("GET /api/observaciones/agenda/{agendaId} -> retorna las observaciones de la agenda")
    public void testBuscarPorAgenda() throws Exception {
        var observacion = new Observacion();
        observacion.setId(1);
        observacion.setAgendaId(5);

        when(observacionService.buscarPorAgenda(5)).thenReturn(List.of(observacion));

        mockMvc.perform(get("/api/observaciones/agenda/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].agendaId").value(5));
    }

    @Test
    @DisplayName("DELETE /api/observaciones/{id} -> elimina una observacion existente")
    public void testEliminar() throws Exception {
        when(observacionService.eliminar(1)).thenReturn(true);

        mockMvc.perform(delete("/api/observaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Observación eliminada correctamente"));
    }
@Test
    @DisplayName("PUT /api/observaciones/{id} -> actualiza una observacion existente")
    public void testActualizar() throws Exception {
        var observacion = new Observacion();
        observacion.setId(1);
        observacion.setDescripcion("Actualizada");

        when(observacionService.actualizar(anyInt(), any(ObservacionDTO.class)))
                .thenReturn(Optional.of(observacion));

        String json = """
                {
                    "agendaId": 5,
                    "descripcion": "Actualizada"
                }
                """;

        mockMvc.perform(put("/api/observaciones/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descripcion").value("Actualizada"));
    }

    @Test
    @DisplayName("PUT /api/observaciones/{id} -> retorna 404 cuando no existe")
    public void testActualizarNoExiste() throws Exception {
        when(observacionService.actualizar(anyInt(), any(ObservacionDTO.class)))
                .thenReturn(Optional.empty());

        String json = """
                {
                    "agendaId": 5,
                    "descripcion": "No importa"
                }
                """;

        mockMvc.perform(put("/api/observaciones/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe la observación con ID: 99"));
    }

}
