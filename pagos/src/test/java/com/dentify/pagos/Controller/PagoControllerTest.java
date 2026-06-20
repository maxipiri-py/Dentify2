package com.dentify.pagos.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
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

import com.dentify.pagos.DTO.PagoDTO;
import com.dentify.pagos.Model.PagoModel;
import com.dentify.pagos.Service.PagoService;

@WebMvcTest(PagoController.class)
public class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PagoService pagoService;

    // =====================================================
    // POST - OK
    // =====================================================

    @Test
    @DisplayName("POST /api/pagos -> registra el pago correctamente")
    void registrarPagoOK() throws Exception {

        when(pagoService.registrarPago(any(PagoDTO.class)))
                .thenReturn(true);

        String json = """
        {
            "pacienteId": 1,
            "nombrePaciente": "Maximiliano Caceres",
            "rutPaciente": "12345678-9",
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

    // =====================================================
    // POST - ERROR
    // =====================================================

    @Test
    @DisplayName("POST /api/pagos -> retorna 400 cuando los datos son inválidos")
    void registrarPagoError() throws Exception {

        doThrow(new IllegalArgumentException(
                "El monto pagado no puede ser mayor al total."))
                .when(pagoService)
                .registrarPago(any(PagoDTO.class));

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
                .andExpect(content().string(
                        "El monto pagado no puede ser mayor al total."));
    }

    // =====================================================
    // GET LISTA - OK
    // =====================================================

    @Test
    @DisplayName("GET /api/pagos/lista-pagos -> retorna listado completo")
    void listarPagosOK() throws Exception {

        PagoModel pago = new PagoModel();
        pago.setId(1);
        pago.setNombrePaciente("Maximiliano Caceres");
        pago.setMontoPagado(35000.0);

        when(pagoService.buscarTodos())
                .thenReturn(List.of(pago));

        mockMvc.perform(get("/api/pagos/lista-pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombrePaciente")
                        .value("Maximiliano Caceres"));
    }

    // =====================================================
    // GET LISTA - VACÍA
    // =====================================================

    @Test
    @DisplayName("GET /api/pagos/lista-pagos -> retorna lista vacía")
    void listarPagosVacia() throws Exception {

        when(pagoService.buscarTodos())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/pagos/lista-pagos"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    // =====================================================
    // GET ESTADO - OK
    // =====================================================

    @Test
    @DisplayName("GET /api/pagos/estado-pago -> retorna pagos filtrados")
    void buscarEstadoOK() throws Exception {

        PagoModel pago = new PagoModel();
        pago.setId(1);
        pago.setEstadoPago("PAGADO");

        when(pagoService.buscarPorEstado("PAGADO"))
                .thenReturn(List.of(pago));

        mockMvc.perform(
                get("/api/pagos/estado-pago")
                        .param("estado", "PAGADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estadoPago")
                        .value("PAGADO"));
    }

    // =====================================================
    // GET ESTADO - VACÍO
    // =====================================================

    @Test
    @DisplayName("GET /api/pagos/estado-pago -> sin resultados")
    void buscarEstadoVacio() throws Exception {

        when(pagoService.buscarPorEstado("PENDIENTE"))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(
                get("/api/pagos/estado-pago")
                        .param("estado", "PENDIENTE"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    // =====================================================
    // PUT - OK
    // =====================================================

    @Test
    @DisplayName("PUT /api/pagos/{id} -> actualiza correctamente")
    void actualizarPagoOK() throws Exception {

        PagoModel pago = new PagoModel();

        pago.setId(1);
        pago.setPacienteId(1);
        pago.setNombrePaciente("Maximiliano Caceres");
        pago.setRutPaciente("12345678-9");
        pago.setEstadoPago("PAGADO");

        when(pagoService.actualizarPago(
                anyInt(),
                any(PagoDTO.class)))
                .thenReturn(Optional.of(pago));

        String json = """
        {
            "pacienteId":1,
            "nombrePaciente":"Maximiliano Caceres",
            "rutPaciente":"12345678-9",
            "presupuestoId":1,
            "montoPagado":35000,
            "montoTotal":35000,
            "estadoPago":"PAGADO"
        }
        """;

        mockMvc.perform(put("/api/pagos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estadoPago").value("PAGADO"));
    }

    // =====================================================
    // PUT - 404
    // =====================================================

    @Test
    @DisplayName("PUT /api/pagos/{id} -> retorna 404 cuando no existe")
    void actualizarPagoNoExiste() throws Exception {

        when(pagoService.actualizarPago(
                anyInt(),
                any(PagoDTO.class)))
                .thenReturn(Optional.empty());

        String json = """
        {
            "pacienteId":1,
            "rutPaciente":"12345678-9",
            "presupuestoId":1,
            "montoPagado":35000,
            "montoTotal":35000
        }
        """;

        mockMvc.perform(put("/api/pagos/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        "No existe el pago con ID: 999"));
    }

    // =====================================================
    // DELETE - OK
    // =====================================================

    @Test
    @DisplayName("DELETE /api/pagos/{id} -> elimina correctamente")
    void eliminarPagoOK() throws Exception {

        when(pagoService.eliminarPago(1))
                .thenReturn(true);

        mockMvc.perform(delete("/api/pagos/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Pago eliminado correctamente"));
    }

    // =====================================================
    // DELETE - 404
    // =====================================================

    @Test
    @DisplayName("DELETE /api/pagos/{id} -> retorna 404 cuando no existe")
    void eliminarPagoNoExiste() throws Exception {

        when(pagoService.eliminarPago(999))
                .thenReturn(false);

        mockMvc.perform(delete("/api/pagos/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        "No existe el pago con ID: 999"));
    }
}