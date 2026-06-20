package com.dentify.dentista.Controller;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;

import com.dentify.dentista.Model.Dentista;


import com.dentify.dentista.DTO.DentistaDTO;
import com.dentify.dentista.Service.DentistaService;

@SpringBootTest
public class DentistaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DentistaController dentistaController;
    
    @Test
    @DisplayName("GET /api/dentista/{id} -> Retorna 200 y JSON si el ID existe")

    public void testBuscarDentista() throws Exception {
    // aqui va lo mismo del dTO 

        var tipoFalso = new Dentista();
        tipoFalso.setId(1);
        tipoFalso.setNombre("Dr. Maxi Piri");
        tipoFalso.setApellido("Piri");
        tipoFalso.setRut("12345678-9");
        tipoFalso.setEspecialidad("Ortodoncia");
        tipoFalso.setCorreo("maxipiri@gmail.com");
        tipoFalso.setTelefono("123456789");

        when(dentistaController.buscarPorId(anyInt())).thenReturn(Optional.of(tipoFalso));
        mockMvc.perform(get("/api/dentista/1"))
                .contentType(MediaType.APPLICATION_JSON)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Dr. Maxi Piri"))
                .andExpect(jsonPath("$.apellido").value("Piri"))
                .andExpect(jsonPath("$.rut").value("12345678-9"))
                .andExpect(jsonPath("$.especialidad").value("Ortodoncia"))
                .andExpect(jsonPath("$.correo").value("maxipiri@gmail.com"))
                .andExpect(jsonPath("$.telefono").value("123456789"));
    }
    private Object print() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'print'");
    }
    private Object status() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'status'");
    }
    @Test
    @DisplayName("Get /api/dentista/{id} -> Retorna 404 y mensaje si el ID no existe")
    public void buscarPorId_cuandoNoExiste_deberiaRetornar404YMensaje() throws Exception {
        when(dentistaController.buscarPorId(anyInt())).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/dentista/99"))
                .contentType(MediaType.APPLICATION_JSON)
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(jsonPath("$.mensaje").value("Dentista no encontrado con ID: 99"));
    }
    @Test
    @DisplayName("POST /api/dentista -> Retorna 200 y JSON con el objeto creado")
    public void CrearDentista_DeberiaRetornarObjetoCreado() throws Exception {
        var dentistaDTO = new DentistaDTO();
        dentistaDTO.setNombre("Dr. Matias Sandoval");
        dentistaDTO.setApellido("Sandoval");
        dentistaDTO.setRut("33336666-9");
        dentistaDTO.setEspecialidad("Urulogo");
        dentistaDTO.setCorreo("matiassandoval@gmail.com");
        dentistaDTO.setTelefono("123456788");

        String jsonRequestBody = """
        {
    "nombre": "Dr. Matias Sandoval",
    "apellido": "Sandoval",
    "rut": "33336666-9",
    "especialidad": "Implantologia",
    "correo": "matiassandoval@gmail.com",
    "telefono": "123456788"
}
        """;
        mockMvc.perform(post("/api/dentista")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("Dr. Matias Sandoval"))
                .andExpect(jsonPath("$.apellido").value("Sandoval"))
                .andExpect(jsonPath("$.rut").value("33336666-9"))
                .andExpect(jsonPath("$.especialidad").value("Urulogo"))
                .andExpect(jsonPath("$.correo").value("matiassandoval@gmail.com"))
                .andExpect(jsonPath("$.telefono").value("123456788"));
    }
}
