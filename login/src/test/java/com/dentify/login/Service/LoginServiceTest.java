package com.dentify.login.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dentify.login.DTO.LoginDTO;
import com.dentify.login.DTO.UsuarioDTO;
import com.dentify.login.Model.Usuario;
import com.dentify.login.Repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @InjectMocks
    private LoginService loginService;

    private UsuarioDTO usuarioDTO(String password) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("maxi");
        dto.setPassword(password);
        dto.setRol("ADMIN");
        return dto;
    }

    private LoginDTO loginDTO(String password) {
        LoginDTO dto = new LoginDTO();
        dto.setUsername("maxi");
        dto.setPassword(password);
        return dto;
    }

    @Test
    @DisplayName("crearUsuario -> guarda la contraseña hasheada con BCrypt")
    public void testCrearUsuarioHasheaPassword() {
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario creado = loginService.crearUsuario(usuarioDTO("1234"));

        assertThat(creado.getPassword()).isNotEqualTo("1234").startsWith("$2");
        assertThat(passwordEncoder.matches("1234", creado.getPassword())).isTrue();
    }

    @Test
    @DisplayName("crearUsuario -> rechaza contraseña vacía")
    public void testCrearUsuarioSinPassword() {
        assertThatThrownBy(() -> loginService.crearUsuario(usuarioDTO(" ")))
                .isInstanceOf(IllegalArgumentException.class);

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("login -> correcto cuando la contraseña coincide con el hash")
    public void testLoginCorrecto() {
        Usuario usuario = new Usuario(1, "maxi", passwordEncoder.encode("1234"), "ADMIN");
        when(usuarioRepository.findByUsername("maxi")).thenReturn(Optional.of(usuario));

        assertThat(loginService.login(loginDTO("1234"))).isEqualTo("Login correcto");
    }

    @Test
    @DisplayName("login -> incorrecto con contraseña errónea o nula")
    public void testLoginIncorrecto() {
        Usuario usuario = new Usuario(1, "maxi", passwordEncoder.encode("1234"), "ADMIN");
        when(usuarioRepository.findByUsername("maxi")).thenReturn(Optional.of(usuario));

        assertThat(loginService.login(loginDTO("otra"))).isEqualTo("Usuario o contraseña incorrectos");
        assertThat(loginService.login(loginDTO(null))).isEqualTo("Usuario o contraseña incorrectos");
    }

    @Test
    @DisplayName("login -> incorrecto cuando el usuario no existe")
    public void testLoginUsuarioNoExiste() {
        when(usuarioRepository.findByUsername("maxi")).thenReturn(Optional.empty());

        assertThat(loginService.login(loginDTO("1234"))).isEqualTo("Usuario o contraseña incorrectos");
    }

    @Test
    @DisplayName("actualizar -> hashea la nueva contraseña")
    public void testActualizarHasheaPassword() {
        Usuario existente = new Usuario(1, "maxi", passwordEncoder.encode("vieja"), "ADMIN");
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario actualizado = loginService.actualizar(1, usuarioDTO("nueva")).orElseThrow();

        assertThat(passwordEncoder.matches("nueva", actualizado.getPassword())).isTrue();
    }

    @Test
    @DisplayName("actualizar -> conserva el hash si no viene contraseña")
    public void testActualizarSinPasswordConservaHash() {
        String hashActual = passwordEncoder.encode("vieja");
        Usuario existente = new Usuario(1, "maxi", hashActual, "ADMIN");
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario actualizado = loginService.actualizar(1, usuarioDTO(null)).orElseThrow();

        assertThat(actualizado.getPassword()).isEqualTo(hashActual);
    }
}
