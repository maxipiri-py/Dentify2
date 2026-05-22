package com.dentify.login.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dentify.login.DTO.LoginDTO;
import com.dentify.login.Model.Usuario;
import com.dentify.login.Repository.UsuarioRepository;

@Service
public class LoginService {

    @Autowired
    private UsuarioRepository usuarioRepository;

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
}
