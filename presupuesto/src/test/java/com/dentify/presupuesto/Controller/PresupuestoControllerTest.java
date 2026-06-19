package com.dentify.presupuesto.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.dentify.presupuesto.Model.PresupuestoModel;
import com.dentify.presupuesto.Service.PresupuestoService;

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
    @DisplayName("GET /api/presupuesto/estado -> retorna los presupuestos filtrados por estado")
    public void testBuscarPorEstado() throws Exception {
        var presupuesto = new PresupuestoModel();
        presupuesto.setId(1);
        presupuesto.setEstadoPago("PAGADO");

        when(presupuestoService.buscarPorEstado("PAGADO")).thenReturn(List.of(presupuesto));

        mockMvc.perform(get("/api/presupuesto/estado")
                .param("estado", "PAGADO")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estadoPago").value("PAGADO"));
    }
}
