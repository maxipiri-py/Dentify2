package com.dentify.paciente.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.dentify.paciente.DTO.PacienteDTO;
import com.dentify.paciente.Model.PacienteModel;
import com.dentify.paciente.Service.PacienteService;

@WebMvcTest(PacienteController.class)
public class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PacienteService pacienteService;

    @Test
    @DisplayName("GET /api/paciente -> retorna el listado completo")
    public void testBuscarTodos() throws Exception {
        var paciente = new PacienteModel();
        paciente.setId(1);
        paciente.setNombre("Maximiliano");
        paciente.setRut("12345678-9");

        when(pacienteService.buscarTodos()).thenReturn(List.of(paciente));

        mockMvc.perform(get("/api/paciente")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Maximiliano"));
    }

    @Test
    @DisplayName("GET /api/paciente -> retorna 204 cuando no hay pacientes")
    public void testBuscarTodosVacio() throws Exception {
        when(pacienteService.buscarTodos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/paciente")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /api/paciente -> crea un paciente nuevo")
    public void testCrearPaciente() throws Exception {
        var paciente = new PacienteModel();
        paciente.setId(1);
        paciente.setNombre("Maximiliano");
        paciente.setApellido("Caceres");
        paciente.setRut("12345678-9");

        when(pacienteService.crear(any(PacienteDTO.class))).thenReturn(paciente);

        String json = """
                {
                    "nombre": "Maximiliano",
                    "apellido": "Caceres",
                    "rut": "12345678-9",
                    "correo": "maxi@gmail.com"
                }
                """;

        mockMvc.perform(post("/api/paciente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Maximiliano"));
    }

    @Test
    @DisplayName("GET /api/paciente/{id} -> retorna el historial cuando el paciente existe")
    public void testBuscarPaciente() throws Exception {
        var paciente = new PacienteModel();
        paciente.setId(1);
        paciente.setNombre("Maximiliano");

        when(pacienteService.obtenerHistorialCompleto(anyInt())).thenReturn(paciente);

        mockMvc.perform(get("/api/paciente/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("GET /api/paciente/{id} -> retorna 404 cuando el paciente no existe")
    public void testBuscarPacienteNoExiste() throws Exception {
        when(pacienteService.obtenerHistorialCompleto(99))
                .thenThrow(new RuntimeException("No existe el paciente con ID: 99"));

        mockMvc.perform(get("/api/paciente/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe el paciente con ID: 99"));
    }
}
