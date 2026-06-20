package com.dentify.presupuesto.Controller;

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
import org.springframework.test.web.servlet.RequestBuilder;

import com.dentify.presupuesto.Service.PresupuestoService;
import com.dentify.presupuesto.Model.PresupuestoModel;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PresupuestoController.class)
public class PresupuestoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PresupuestoService presupuestoService;

    @Test
    @DisplayName("POST /api/presupuesto/crear-presupuesto -> retorna 201 cuando se crea correctamente")
    public void testCrearPresupuesto() throws Exception {
        when(presupuestoService.guardarPresupuesto(any(PresupuestoModel.class))).thenReturn(true);

        String json = """
                {
                    "pacienteId": 1,
                    "dentistaId": 1,
                    "prestacionId": 1,
                    "nombrePaciente": "Maximiliano Caceres",
                    "rutPaciente": "12345678-9",
                    "tratamiento": "Limpieza dental",
                    "montoTotal": 35000.0,
                    "fechaEmision": "2026-06-18",
                    "estadoPago": "PENDIENTE"
                }
                """;

        mockMvc.perform(post("/api/presupuesto/crear-presupuesto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string("Presupuesto creado correctamente."));
    }

    @Test
    @DisplayName("POST /api/presupuesto/crear-presupuesto -> retorna 500 cuando falla el guardado")
    public void testCrearPresupuestoFalla() throws Exception {
        when(presupuestoService.guardarPresupuesto(any(PresupuestoModel.class))).thenReturn(false);

        String json = """
                {
                    "pacienteId": 1,
                    "dentistaId": 1,
                    "prestacionId": 1
                }
                """;

        mockMvc.perform(post("/api/presupuesto/crear-presupuesto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error: No se pudo crear el presupuesto en la base de datos."));
    }

    @Test
    @DisplayName("GET /api/presupuesto/{id} -> retorna el presupuesto cuando existe")
    public void testBuscarPorId() throws Exception {
        var presupuesto = new PresupuestoModel();
        presupuesto.setId(1);
        presupuesto.setNombrePaciente("Maximiliano Caceres");
        presupuesto.setMontoTotal(35000.0);
        presupuesto.setEstadoPago("PENDIENTE");

        when(presupuestoService.buscarPorId(1)).thenReturn(Optional.of(presupuesto));

        mockMvc.perform(get("/api/presupuesto/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombrePaciente").value("Maximiliano Caceres"));
    }

    @Test
    @DisplayName("GET /api/presupuesto/{id} -> retorna 404 cuando no existe")
    public void testBuscarPorIdNoExiste() throws Exception {
        when(presupuestoService.buscarPorId(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/presupuesto/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("No existe el presupuesto con ID: 99"));
    }

    @Test
    @DisplayName("DELETE /api/presupuesto/{id} -> elimina un presupuesto existente")
    public void testEliminarPresupuesto() throws Exception {
        when(presupuestoService.eliminar(1)).thenReturn(true);

        mockMvc.perform(delete("/api/presupuesto/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Presupuesto eliminado correctamente"));
    }

    private RequestBuilder delete(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Test
    @DisplayName("DELETE /api/presupuesto/{id} -> retorna 404 cuando no existe")
    public void testEliminarPresupuestoNoExiste() throws Exception {
        when(presupuestoService.eliminar(99)).thenReturn(false);

        mockMvc.perform(delete("/api/presupuesto/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("No existe el presupuesto con ID: 99"));
    }

    @Test
    @DisplayName("PUT /api/presupuesto/{id} -> actualiza un presupuesto existente")
    public void testActualizarPresupuesto() throws Exception {
        var presupuesto = new PresupuestoModel();
        presupuesto.setId(1);
        presupuesto.setNombrePaciente("Maximiliano Caceres");        
        when(presupuestoService.actualizar(anyInt(), any(PresupuestoModel.class)))
                .thenReturn(Optional.of(presupuesto));

        String json = """
                {
                    "nombrePaciente": "Maximiliano Caceres"
                }
                """;

        mockMvc.perform(put("/api/presupuesto/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombrePaciente").value("Maximiliano Caceres"));
    }

    @Test
    @DisplayName("PUT /api/presupuesto/{id} -> retorna 404 cuando no existe")
    public void testActualizarPresupuestoNoExiste() throws Exception {
        when(presupuestoService.actualizar(anyInt(), any(PresupuestoModel.class)))
                .thenReturn(Optional.empty());

        String json = "{}";

        mockMvc.perform(put("/api/presupuesto/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe el presupuesto con ID: 99"));
    }

}
