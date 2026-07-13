package com.dentify.agenda.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import com.dentify.agenda.DTO.AgendaDTO;
import com.dentify.agenda.Service.AgendaService;

@WebMvcTest(AgendaController.class)
public class AgendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AgendaService agendaService;

    @Test
    @DisplayName("POST /api/agenda/crear-agenda -> retorna 201 cuando se crea correctamente")
    public void testCrearAgenda() throws Exception {
        when(agendaService.guardarAgenda(any(AgendaDTO.class))).thenReturn(true);

        String json = """
                {
                    "pacienteId": 1,
                    "dentistaId": 1,
                    "nombre": "Maximiliano",
                    "apellido": "Caceres",
                    "rut": "12345678-9",
                    "especialidad": "Ortodoncia",
                    "fechaHora": "2026-06-20 10:00"
                }
                """;

        mockMvc.perform(post("/api/agenda/crear-agenda")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string("Agenda creada correctamente."));
    }

    @Test
    @DisplayName("POST /api/agenda/crear-agenda -> retorna 500 cuando falla el guardado")
    public void testCrearAgendaFalla() throws Exception {
        when(agendaService.guardarAgenda(any(AgendaDTO.class))).thenReturn(false);

        String json = """
                {
                    "pacienteId": 1,
                    "dentistaId": 1
                }
                """;

        mockMvc.perform(post("/api/agenda/crear-agenda")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error: No se pudo guardar la agenda en la base de datos."));
    }

    @Test
    @DisplayName("GET /api/agenda/listar -> retorna el listado completo")
    public void testListarTodos() throws Exception {
        var agenda = new AgendaDTO();
        agenda.setId(1);
        agenda.setNombre("Maximiliano");
        agenda.setFechaHora("2026-06-20 10:00");

        when(agendaService.buscarTodos()).thenReturn(java.util.List.of(agenda));

        mockMvc.perform(get("/api/agenda/listar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Maximiliano"));
    }

    @Test
    @DisplayName("GET /api/agenda/{id} -> retorna la agenda cuando existe")
    public void testBuscarPorId() throws Exception {
        var agenda = new AgendaDTO();
        agenda.setId(1);
        agenda.setNombre("Maximiliano");
        agenda.setEspecialidad("Ortodoncia");

        when(agendaService.buscarPorId(1)).thenReturn(Optional.of(agenda));

        mockMvc.perform(get("/api/agenda/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.especialidad").value("Ortodoncia"));
    }

    @Test
    @DisplayName("GET /api/agenda/{id} -> retorna 404 cuando no existe")
    public void testBuscarPorIdNoExiste() throws Exception {
        when(agendaService.buscarPorId(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/agenda/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe la agenda con ID: 99"));
    }

    @Test
    @DisplayName("PUT /api/agenda/{id} -> retorna 200 cuando se actualiza correctamente")
    public void testActualizarAgenda() throws Exception {
        var agenda = new AgendaDTO();
        agenda.setNombre("Maximiliano Actualizado");
        when(agendaService.actualizar(anyInt(), any(AgendaDTO.class))).thenReturn(Optional.of(agenda));

        mockMvc.perform(put("/api/agenda/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Maximiliano Actualizado"));
    }

    @Test
    @DisplayName("PUT /api/agenda/{id} -> retorna 404 cuando no existe")
    public void testActualizarAgendaNoExiste() throws Exception {
        when(agendaService.actualizar(anyInt(), any(AgendaDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/agenda/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/agenda/{id} -> retorna 200 cuando se elimina")
    public void testEliminarAgenda() throws Exception {
        when(agendaService.eliminar(1)).thenReturn(true);

        mockMvc.perform(delete("/api/agenda/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/agenda/{id} -> retorna 404 cuando no existe")
    public void testEliminarAgendaNoExiste() throws Exception {
        when(agendaService.eliminar(99)).thenReturn(false);

        mockMvc.perform(delete("/api/agenda/99"))
                .andExpect(status().isNotFound());
    }
}