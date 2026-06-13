package com.dentify.login.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dentify.login.DTO.LoginDTO;
import com.dentify.login.DTO.UsuarioDTO;
import com.dentify.login.Model.Usuario;
import com.dentify.login.Repository.UsuarioRepository;

@Service
public class LoginService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // LOGIN
    public String login(LoginDTO dto){

        Optional<Usuario> usuario =
                usuarioRepository.findByUsername(dto.getUsername());

        if(usuario.isPresent()
                && usuario.get().getPassword()
                .equals(dto.getPassword())){

            return "Login correcto";
        }

        return "Usuario o contraseña incorrectos";
    }

    // CREAR USUARIO
    public Usuario crearUsuario(UsuarioDTO dto){

        Usuario usuario = new Usuario();

        usuario.setUsername(dto.getUsername());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(dto.getRol());

        return usuarioRepository.save(usuario);
    }

    // BUSCAR POR ID
    public Optional<Usuario> buscarPorId(Integer id){
        return usuarioRepository.findById(id);
    }

    // LISTAR TODOS
    public List<Usuario> listarTodos(){
        return usuarioRepository.findAll();
    }

    // ACTUALIZAR
    public Optional<Usuario> actualizar(Integer id, UsuarioDTO dto){

        Optional<Usuario> usuarioOptional =
                usuarioRepository.findById(id);

        if(usuarioOptional.isEmpty()){
            return Optional.empty();
        }

        Usuario usuario = usuarioOptional.get();

        usuario.setUsername(dto.getUsername());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(dto.getRol());

        Usuario usuarioActualizado =
                usuarioRepository.save(usuario);

        return Optional.of(usuarioActualizado);
    }

    // ELIMINAR
    public boolean eliminar(Integer id){

        Optional<Usuario> usuario =
                usuarioRepository.findById(id);

        if(usuario.isEmpty()){
            return false;
        }

        usuarioRepository.deleteById(id);

        return true;
    }
}