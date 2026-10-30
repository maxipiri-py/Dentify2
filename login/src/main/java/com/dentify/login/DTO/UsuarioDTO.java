package com.dentify.login.DTO;

import lombok.Data;
import lombok.ToString;

@Data
public class UsuarioDTO {

    private String username;

    @ToString.Exclude
    private String password;

    private String rol;
}
