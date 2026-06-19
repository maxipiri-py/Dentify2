package com.dentify.reporte.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.dentify.reporte.DTO.ReporteDTO;
import com.dentify.reporte.Service.ReporteService;

@WebMvcTest(ReporteController.class)
public class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReporteService reporteService;

    @Test
    @DisplayName("POST /api/reporte/generar-reporte -> retorna 201 cuando se genera correctamente")
    public void testGenerarReporte() throws Exception {
        when(reporteService.generarReporte(any(ReporteDTO.class))).thenReturn(true);

        String json = """
                {
                    "tipoReporte": "MENSUAL",
                    "descripcion": "Reporte de junio",
                    "fechaGeneracion": "2026-06-18",
                    "totalPacientes": 10,
                    "totalCitas": 15,
                    "totalIngresos": 250000.0
                }
                """;

        mockMvc.perform(post("/api/reporte/generar-reporte")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string("Reporte generado correctamente."));
    }

    @Test
    @DisplayName("POST /api/reporte/generar-reporte -> retorna 500 cuando falla la generación")
    public void testGenerarReporteFalla() throws Exception {
        when(reporteService.generarReporte(any(ReporteDTO.class))).thenReturn(false);

        String json = """
                {
                    "tipoReporte": "MENSUAL",
                    "descripcion": "Reporte de junio"
                }
                """;

        mockMvc.perform(post("/api/reporte/generar-reporte")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al generar el reporte. Revisa la consola para más detalles."));
    }

    @Test
    @DisplayName("GET /api/reporte/{id} -> retorna el reporte cuando existe")
    public void testBuscarPorId() throws Exception {
        var reporte = new ReporteDTO();
        reporte.setId(1);
        reporte.setTipoReporte("MENSUAL");
        reporte.setDescripcion("Reporte de junio");
        reporte.setTotalPacientes(10);

        when(reporteService.buscarPorId(1)).thenReturn(Optional.of(reporte));

        mockMvc.perform(get("/api/reporte/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipoReporte").value("MENSUAL"));
    }

    @Test
    @DisplayName("GET /api/reporte/{id} -> retorna 404 cuando no existe")
    public void testBuscarPorIdNoExiste() throws Exception {
        when(reporteService.buscarPorId(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reporte/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("No existe el reporte con ID: 99"));
    }
}
