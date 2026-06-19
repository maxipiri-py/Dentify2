package com.dentify.pagos.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.dentify.pagos.DTO.PagoDTO;
import com.dentify.pagos.Model.PagoModel;
import com.dentify.pagos.Service.PagoService;

@WebMvcTest(PagoController.class)
public class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PagoService pagoService;

    @Test
    @DisplayName("POST /api/pagos -> registra el pago correctamente")
    public void testRegistrarPago() throws Exception {
        String json = """
                {
                    "pacienteId": 1,
                    "nombrePaciente": "Maximiliano Caceres",
                    "presupuestoId": 1,
                    "montoPagado": 35000.0,
                    "montoTotal": 35000.0,
                    "fechaPago": "2026-06-18",
                    "metodoPago": "EFECTIVO",
                    "estadoPago": "PAGADO"
                }
                """;

        mockMvc.perform(post("/api/pagos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string("Pago registrado correctamente."));
    }

    @Test
    @DisplayName("POST /api/pagos -> retorna 400 cuando los datos son invalidos")
    public void testRegistrarPagoDatosInvalidos() throws Exception {
        doThrow(new IllegalArgumentException("El monto pagado no puede ser mayor al total"))
                .when(pagoService).registrarPago(any(PagoDTO.class));

        String json = """
                {
                    "pacienteId": 1,
                    "montoPagado": 99999.0,
                    "montoTotal": 35000.0
                }
                """;

        mockMvc.perform(post("/api/pagos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El monto pagado no puede ser mayor al total"));
    }

    @Test
    @DisplayName("GET /api/pagos/lista-pagos -> retorna el listado completo")
    public void testListarPagos() throws Exception {
        var pago = new PagoModel();
        pago.setId(1);
        pago.setNombrePaciente("Maximiliano Caceres");
        pago.setMontoPagado(35000.0);

        when(pagoService.buscarTodos()).thenReturn(List.of(pago));

        mockMvc.perform(get("/api/pagos/lista-pagos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombrePaciente").value("Maximiliano Caceres"));
    }

    @Test
    @DisplayName("GET /api/pagos/estado-pago -> retorna los pagos filtrados por estado")
    public void testBuscarPorEstado() throws Exception {
        var pago = new PagoModel();
        pago.setId(1);
        pago.setEstadoPago("PAGADO");

        when(pagoService.buscarPorEstado("PAGADO")).thenReturn(List.of(pago));

        mockMvc.perform(get("/api/pagos/estado-pago")
                .param("estado", "PAGADO")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estadoPago").value("PAGADO"));
    }
}
