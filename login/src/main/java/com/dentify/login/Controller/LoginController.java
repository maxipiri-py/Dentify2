package com.dentify.login.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dentify.login.DTO.LoginDTO;
import com.dentify.login.Service.LoginService;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<String> login(
            @RequestBody LoginDTO dto){

        return ResponseEntity.ok(
                loginService.login(dto)
        );
    }
}
