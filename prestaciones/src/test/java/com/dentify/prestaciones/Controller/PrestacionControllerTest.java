package com.dentify.prestaciones.Controller;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.dentify.prestaciones.DTO.PrestacionDTO;
import com.dentify.prestaciones.Model.Prestacion;
import com.dentify.prestaciones.Service.PrestacionService;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PrestacionController.class)
public class PrestacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PrestacionService prestacionService;

    @Test
    @DisplayName("POST /api/prestaciones -> crea una prestacion nueva")
    public void testCrear() throws Exception {
        var prestacion = new Prestacion();
        prestacion.setId(1);
        prestacion.setNombre("Limpieza dental");
        prestacion.setDescripcion("Profilaxis completa");
        prestacion.setValor(20000);

        when(prestacionService.crear(any(PrestacionDTO.class))).thenReturn(prestacion);

        String json = """
                {
                    "nombre": "Limpieza dental",
                    "descripcion": "Profilaxis completa",
                    "valor": 20000
                }
                """;

        mockMvc.perform(post("/api/prestaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Limpieza dental"));
    }
    
    @Test
    @DisplayName("POST /api/prestaciones -> retorna 400 cuando los datos son inválidos")
    public void testCrearError() throws Exception {
        when(prestacionService.crear(any(PrestacionDTO.class))).thenThrow(new IllegalArgumentException("Error"));

        mockMvc.perform(post("/api/prestaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }



    @Test
    @DisplayName("GET /api/prestaciones -> retorna el listado completo")
    public void testListar() throws Exception {
        var prestacion = new Prestacion();
        prestacion.setId(1);
        prestacion.setNombre("Limpieza dental");

        when(prestacionService.listar()).thenReturn(List.of(prestacion));

        mockMvc.perform(get("/api/prestaciones")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Limpieza dental"));
    }



    @Test
    @DisplayName("GET /api/prestaciones/{id} -> retorna 404 cuando no existe")
    public void testBuscarPorIdNoExiste() throws Exception {
        when(prestacionService.buscarPorId(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/prestaciones/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("No existe la prestación con ID: 99"));
    }

    @Test
    @DisplayName("PUT /api/prestaciones/{id} -> actualiza una prestacion existente")
    public void testActualizar() throws Exception {
        var prestacion = new Prestacion();
        prestacion.setId(1);
        prestacion.setNombre("Limpieza dental premium");
        prestacion.setValor(25000);

        when(prestacionService.actualizar(eq(1), any(PrestacionDTO.class))).thenReturn(Optional.of(prestacion));

        String json = """
                {
                    "nombre": "Limpieza dental premium",
                    "valor": 25000
                }
                """;

        mockMvc.perform(put("/api/prestaciones/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Limpieza dental premium"));
    }

    @Test
    @DisplayName("PUT /api/prestaciones/{id} -> retorna 404 cuando no existe")
    public void testActualizarNoExiste() throws Exception {

        mockMvc.perform(put("/api/prestaciones/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("DELETE /api/prestaciones/{id} -> elimina una prestacion existente")
    public void testEliminar() throws Exception {
        when(prestacionService.eliminar(1)).thenReturn(true);

        mockMvc.perform(delete("/api/prestaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Prestación eliminada correctamente"));
    }

    @Test
    @DisplayName("DELETE /api/prestaciones/{id} -> retorna 404 cuando no existe")
    public void testEliminarNoExiste() throws Exception {

        mockMvc.perform(delete("/api/prestaciones/99"))
                .andExpect(status().isNotFound());
    }

}
