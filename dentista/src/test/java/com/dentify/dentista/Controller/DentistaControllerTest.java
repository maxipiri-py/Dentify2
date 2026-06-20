package com.dentify.dentista.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dentify.dentista.DTO.DentistaDTO;
import com.dentify.dentista.Model.Dentista;
import com.dentify.dentista.Service.DentistaService;

@SpringBootTest
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
public class DentistaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DentistaService dentistaService;

    // =====================================================
    // GET BY ID - OK
    // =====================================================

    @Test
    @DisplayName("GET /api/dentista/{id} -> Retorna 200 y JSON si el ID existe")
    public void testBuscarDentista() throws Exception {

        Dentista dentista = new Dentista();
        dentista.setId(1);
        dentista.setNombre("Dr. Maxi Piri");
        dentista.setApellido("Piri");
        dentista.setRut("12345678-9");
        dentista.setEspecialidad("Ortodoncia");
        dentista.setCorreo("maxipiri@gmail.com");
        dentista.setTelefono("123456789");

        when(dentistaService.buscarPorId(anyInt()))
                .thenReturn(Optional.of(dentista));

        mockMvc.perform(get("/api/dentista/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Dr. Maxi Piri"))
                .andExpect(jsonPath("$.apellido").value("Piri"))
                .andExpect(jsonPath("$.rut").value("12345678-9"))
                .andExpect(jsonPath("$.especialidad").value("Ortodoncia"))
                .andExpect(jsonPath("$.correo").value("maxipiri@gmail.com"))
                .andExpect(jsonPath("$.telefono").value("123456789"));
    }

    // =====================================================
    // GET BY ID - NOT FOUND
    // =====================================================

    @Test
    @DisplayName("GET /api/dentista/{id} -> Retorna 404 cuando no existe")
    public void buscarPorId_cuandoNoExiste_deberiaRetornar404YMensaje() throws Exception {

        when(dentistaService.buscarPorId(anyInt()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/dentista/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe el dentista con ID: 99"));
    }

    // =====================================================
    // POST - OK
    // =====================================================

    @Test
    @DisplayName("POST /api/dentista -> Retorna 201 y JSON con el objeto creado")
    public void crearDentista_DeberiaRetornarObjetoCreado() throws Exception {

        Dentista dentistaCreado = new Dentista();
        dentistaCreado.setId(2);
        dentistaCreado.setNombre("Dr. Matias Sandoval");
        dentistaCreado.setApellido("Sandoval");
        dentistaCreado.setRut("33336666-9");
        dentistaCreado.setEspecialidad("Urologo");
        dentistaCreado.setCorreo("matiassandoval@gmail.com");
        dentistaCreado.setTelefono("123456788");

        when(dentistaService.crear(any(DentistaDTO.class)))
                .thenReturn(dentistaCreado);

        String jsonRequestBody = """
        {
            "nombre":"Dr. Matias Sandoval",
            "apellido":"Sandoval",
            "rut":"33336666-9",
            "especialidad":"Urologo",
            "correo":"matiassandoval@gmail.com",
            "telefono":"123456788"
        }
        """;

        mockMvc.perform(post("/api/dentista")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("Dr. Matias Sandoval"))
                .andExpect(jsonPath("$.apellido").value("Sandoval"))
                .andExpect(jsonPath("$.rut").value("33336666-9"))
                .andExpect(jsonPath("$.especialidad").value("Urologo"))
                .andExpect(jsonPath("$.correo").value("matiassandoval@gmail.com"))
                .andExpect(jsonPath("$.telefono").value("123456788"));
    }

    // =====================================================
    // POST - BAD REQUEST
    // =====================================================

    @Test
    @DisplayName("POST /api/dentista -> Retorna 400 cuando los datos son inválidos")
    public void crearDentista_DatosInvalidos_DeberiaRetornar400() throws Exception {

        when(dentistaService.crear(any(DentistaDTO.class)))
                .thenThrow(new IllegalArgumentException(
                        "El nombre del dentista no puede estar vacío."));

        String body = """
        {
            "nombre":"",
            "apellido":"Sandoval",
            "rut":"33336666-9"
        }
        """;

        mockMvc.perform(post("/api/dentista")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "El nombre del dentista no puede estar vacío."));
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @Test
    @DisplayName("GET /api/dentista -> Retorna lista de dentistas")
    public void listarDentistas_DeberiaRetornarLista() throws Exception {

        Dentista d1 = new Dentista(
                1,
                "11111111-1",
                "Juan",
                "Perez",
                "Ortodoncia",
                "juan@gmail.com",
                "123456");

        Dentista d2 = new Dentista(
                2,
                "22222222-2",
                "Maria",
                "Gonzalez",
                "Cirugia",
                "maria@gmail.com",
                "987654");

        List<Dentista> lista = Arrays.asList(d1, d2);

        when(dentistaService.listarTodos())
                .thenReturn(lista);

        mockMvc.perform(get("/api/dentista"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[1].nombre").value("Maria"));
    }

    // =====================================================
    // PUT - OK
    // =====================================================

    @Test
    @DisplayName("PUT /api/dentista/{id} -> Retorna dentista actualizado")
    public void actualizarDentista_DeberiaRetornar200() throws Exception {

        Dentista actualizado = new Dentista(
                1,
                "12345678-9",
                "Maximiliano",
                "Piri",
                "Ortodoncia",
                "nuevo@gmail.com",
                "999999999");

        when(dentistaService.actualizar(anyInt(), any(DentistaDTO.class)))
                .thenReturn(Optional.of(actualizado));

        String body = """
        {
            "nombre":"Maximiliano",
            "apellido":"Piri",
            "rut":"12345678-9",
            "especialidad":"Ortodoncia",
            "correo":"nuevo@gmail.com",
            "telefono":"999999999"
        }
        """;

        mockMvc.perform(put("/api/dentista/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Maximiliano"))
                .andExpect(jsonPath("$.correo").value("nuevo@gmail.com"));
    }

    // =====================================================
    // PUT - NOT FOUND
    // =====================================================

    @Test
    @DisplayName("PUT /api/dentista/{id} -> Retorna 404 cuando no existe")
    public void actualizarDentista_NoExiste_DeberiaRetornar404() throws Exception {

        when(dentistaService.actualizar(anyInt(), any(DentistaDTO.class)))
                .thenReturn(Optional.empty());

        String body = """
        {
            "nombre":"Maximiliano",
            "apellido":"Piri",
            "rut":"12345678-9"
        }
        """;

        mockMvc.perform(put("/api/dentista/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        "No existe el dentista con ID: 99"));
    }

    // =====================================================
    // PUT - BAD REQUEST
    // =====================================================

    @Test
    @DisplayName("PUT /api/dentista/{id} -> Retorna 400 cuando los datos son inválidos")
    public void actualizarDentista_DatosInvalidos_DeberiaRetornar400() throws Exception {

        when(dentistaService.actualizar(anyInt(), any(DentistaDTO.class)))
                .thenThrow(new IllegalArgumentException(
                        "El RUT no puede estar vacío."));

        String body = """
        {
            "nombre":"Maximiliano",
            "apellido":"Piri",
            "rut":""
        }
        """;

        mockMvc.perform(put("/api/dentista/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "El RUT no puede estar vacío."));
    }

    // =====================================================
    // DELETE - OK
    // =====================================================

    @Test
    @DisplayName("DELETE /api/dentista/{id} -> Retorna 200 cuando elimina correctamente")
    public void eliminarDentista_DeberiaRetornar200() throws Exception {

        when(dentistaService.eliminar(anyInt()))
                .thenReturn(true);

        mockMvc.perform(delete("/api/dentista/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Dentista eliminado correctamente"));
    }

    // =====================================================
    // DELETE - NOT FOUND
    // =====================================================

    @Test
    @DisplayName("DELETE /api/dentista/{id} -> Retorna 404 cuando no existe")
    public void eliminarDentista_NoExiste_DeberiaRetornar404() throws Exception {

        when(dentistaService.eliminar(anyInt()))
                .thenReturn(false);

        mockMvc.perform(delete("/api/dentista/99"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        "No existe el dentista con ID: 99"));
    }
}