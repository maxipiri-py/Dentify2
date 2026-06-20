package com.dentify.login.Controller;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;

import com.dentify.login.DTO.UsuarioDTO;
import com.dentify.login.Model.Usuario;   

import com.dentify.login.DTO.LoginDTO;
import com.dentify.login.Service.LoginService;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoginService loginService;

    @Test
    @DisplayName("POST /api/login -> retorna el token cuando las credenciales son correctas")
    public void testLogin() throws Exception {
        when(loginService.login(any(LoginDTO.class))).thenReturn("token123");

        String json = """
                {
                    "username": "maxi",
                    "password": "1234"
                }
                """;

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("token123"));
    }

    @Test
    @DisplayName("POST /api/login/usuarios -> crea un usuario nuevo")
    public void testCrearUsuario() throws Exception {
        var usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("maxi");
        usuario.setRol("ADMIN");

        when(loginService.crearUsuario(any(UsuarioDTO.class))).thenReturn(usuario);

        String json = """
                {
                    "username": "maxi",
                    "password": "1234",
                    "rol": "ADMIN"
                }
                """;

        mockMvc.perform(post("/api/login/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("maxi"))
                .andExpect(jsonPath("$.rol").value("ADMIN"));
    }

    @Test
    @DisplayName("GET /api/login/usuarios/{id} -> retorna el usuario cuando existe")
    public void testBuscarUsuario() throws Exception {
        var usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("maxi");

        when(loginService.buscarPorId(1)).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/login/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("maxi"));
    }

    @Test
    @DisplayName("GET /api/login/usuarios/{id} -> retorna 404 cuando no existe")
    public void testBuscarUsuarioNoExiste() throws Exception {
        when(loginService.buscarPorId(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/login/usuarios/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/login/usuarios -> retorna el listado completo")
    public void testListarUsuarios() throws Exception {
        var usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("maxi");

        when(loginService.listarTodos()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/login/usuarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("maxi"));
    }

    @Test
    @DisplayName("GET /api/login/usuarios -> retorna lista vacía")
    public void testListarUsuariosVacio() throws Exception {
        when(loginService.listarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/login/usuarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @DisplayName("DELETE /api/login/usuarios/{id} -> elimina un usuario existente")
    public void testEliminarUsuario() throws Exception {
        when(loginService.eliminar(1)).thenReturn(true);

        mockMvc.perform(delete("/api/login/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario eliminado correctamente"));
    }
}
