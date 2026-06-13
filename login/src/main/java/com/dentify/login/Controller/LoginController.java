package com.dentify.login.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dentify.login.DTO.LoginDTO;
import com.dentify.login.DTO.UsuarioDTO;
import com.dentify.login.Model.Usuario;
import com.dentify.login.Service.LoginService;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    // LOGIN
    @PostMapping
    public ResponseEntity<String> login(
            @RequestBody LoginDTO dto){

        return ResponseEntity.ok(
                loginService.login(dto)
        );
    }

    // CREAR USUARIO
    @PostMapping("/usuarios")
    public ResponseEntity<Usuario> crearUsuario(
            @RequestBody UsuarioDTO dto){

        Usuario usuario =
                loginService.crearUsuario(dto);

        return ResponseEntity.ok(usuario);
    }

    // BUSCAR USUARIO POR ID
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<?> buscarUsuario(
            @PathVariable Integer id){

        Optional<Usuario> usuario =
                loginService.buscarPorId(id);

        if(usuario.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuario.get());
    }

    // LISTAR USUARIOS
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios(){

        return ResponseEntity.ok(
                loginService.listarTodos()
        );
    }

    // ACTUALIZAR USUARIO
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<?> actualizarUsuario(
            @PathVariable Integer id,
            @RequestBody UsuarioDTO dto){

        Optional<Usuario> usuario =
                loginService.actualizar(id, dto);

        if(usuario.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuario.get());
    }

    // ELIMINAR USUARIO
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> eliminarUsuario(
            @PathVariable Integer id){

        boolean eliminado =
                loginService.eliminar(id);

        if(!eliminado){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                "Usuario eliminado correctamente"
        );
    }
}