package com.dentify.paciente.Controller;

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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    // =====================================================
    // GET ALL OK
    // =====================================================

    @Test
    @DisplayName("GET /api/paciente -> retorna listado completo")
    public void testBuscarTodos() throws Exception {

        PacienteModel paciente = new PacienteModel();
        paciente.setId(1);
        paciente.setNombre("Maximiliano");
        paciente.setApellido("Caceres");
        paciente.setRut("12345678-9");

        when(pacienteService.buscarTodos())
                .thenReturn(List.of(paciente));

        mockMvc.perform(get("/api/paciente")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Maximiliano"));
    }

    // =====================================================
    // GET ALL EMPTY
    // =====================================================

    @Test
    @DisplayName("GET /api/paciente -> retorna 204 cuando no existen pacientes")
    public void testBuscarTodosVacio() throws Exception {

        when(pacienteService.buscarTodos())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/paciente"))
                .andExpect(status().isNoContent());
    }

    // =====================================================
    // POST OK
    // =====================================================

    @Test
    @DisplayName("POST /api/paciente -> crea paciente correctamente")
    public void testCrearPaciente() throws Exception {

        PacienteModel paciente = new PacienteModel();

        paciente.setId(1);
        paciente.setNombre("Maximiliano");
        paciente.setApellido("Caceres");
        paciente.setRut("12345678-9");
        paciente.setCorreo("maxi@gmail.com");

        when(pacienteService.crear(any(PacienteDTO.class)))
                .thenReturn(paciente);

        String json = """
        {
            "nombre":"Maximiliano",
            "apellido":"Caceres",
            "rut":"12345678-9",
            "correo":"maxi@gmail.com"
        }
        """;

        mockMvc.perform(post("/api/paciente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Maximiliano"))
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    // =====================================================
    // POST BAD REQUEST
    // =====================================================

    @Test
    @DisplayName("POST /api/paciente -> retorna 400 cuando los datos son inválidos")
    public void testCrearPacienteBadRequest() throws Exception {

        when(pacienteService.crear(any(PacienteDTO.class)))
                .thenThrow(new IllegalArgumentException(
                        "El nombre del paciente no puede estar vacío."));

        String json = """
        {
            "nombre":"",
            "apellido":"Caceres",
            "rut":"12345678-9"
        }
        """;

        mockMvc.perform(post("/api/paciente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "El nombre del paciente no puede estar vacío."));
    }

    // =====================================================
    // GET BY ID OK
    // =====================================================

    @Test
    @DisplayName("GET /api/paciente/{id} -> retorna paciente existente")
    public void testBuscarPaciente() throws Exception {

        PacienteModel paciente = new PacienteModel();

        paciente.setId(1);
        paciente.setNombre("Maximiliano");
        paciente.setApellido("Caceres");

        when(pacienteService.buscarPorId(anyInt()))
                .thenReturn(Optional.of(paciente));

        mockMvc.perform(get("/api/paciente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Maximiliano"));
    }

    // =====================================================
    // GET BY ID 404
    // =====================================================

    @Test
    @DisplayName("GET /api/paciente/{id} -> retorna 404")
    public void testBuscarPacienteNoExiste() throws Exception {

        when(pacienteService.buscarPorId(anyInt()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/paciente/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        "No existe el paciente con ID: 99"));
    }

    // =====================================================
    // PUT OK
    // =====================================================

    @Test
    @DisplayName("PUT /api/paciente/{id} -> actualiza paciente")
    public void testActualizarPaciente() throws Exception {

        PacienteModel paciente = new PacienteModel();

        paciente.setId(1);
        paciente.setNombre("Maximiliano Actualizado");
        paciente.setApellido("Caceres");

        when(pacienteService.actualizar(
                anyInt(),
                any(PacienteDTO.class)))
                .thenReturn(Optional.of(paciente));

        String json = """
        {
            "nombre":"Maximiliano Actualizado",
            "apellido":"Caceres",
            "rut":"12345678-9",
            "correo":"maxi@gmail.com"
        }
        """;

        mockMvc.perform(put("/api/paciente/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre")
                        .value("Maximiliano Actualizado"));
    }

    // =====================================================
    // PUT 404
    // =====================================================

    @Test
    @DisplayName("PUT /api/paciente/{id} -> retorna 404 cuando no existe")
    public void testActualizarPacienteNoExiste() throws Exception {

        when(pacienteService.actualizar(
                anyInt(),
                any(PacienteDTO.class)))
                .thenReturn(Optional.empty());

        String json = """
        {
            "nombre":"Maximiliano",
            "apellido":"Caceres",
            "rut":"12345678-9"
        }
        """;

        mockMvc.perform(put("/api/paciente/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        "No existe el paciente con ID: 99"));
    }

    // =====================================================
    // PUT 400
    // =====================================================

    @Test
    @DisplayName("PUT /api/paciente/{id} -> retorna 400 cuando los datos son inválidos")
    public void testActualizarPacienteBadRequest() throws Exception {

        when(pacienteService.actualizar(
                anyInt(),
                any(PacienteDTO.class)))
                .thenThrow(new IllegalArgumentException(
                        "El RUT no puede estar vacío."));

        String json = """
        {
            "nombre":"Maximiliano",
            "apellido":"Caceres",
            "rut":""
        }
        """;

        mockMvc.perform(put("/api/paciente/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "El RUT no puede estar vacío."));
    }

    // =====================================================
    // DELETE OK
    // =====================================================

    @Test
    @DisplayName("DELETE /api/paciente/{id} -> elimina paciente")
    public void testEliminarPaciente() throws Exception {

        when(pacienteService.eliminar(anyInt()))
                .thenReturn(true);

        mockMvc.perform(delete("/api/paciente/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Paciente eliminado correctamente"));
    }

    // =====================================================
    // DELETE 404
    // =====================================================

    @Test
    @DisplayName("DELETE /api/paciente/{id} -> retorna 404")
    public void testEliminarPacienteNoExiste() throws Exception {

        when(pacienteService.eliminar(anyInt()))
                .thenReturn(false);

        mockMvc.perform(delete("/api/paciente/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        "No existe el paciente con ID: 99"));
    }
}