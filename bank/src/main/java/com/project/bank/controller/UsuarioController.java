package com.project.bank.controller;

import com.project.bank.dto.usuario.request.CadastroRequest;
import com.project.bank.dto.usuario.response.ResponsePadrao;
import com.project.bank.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastro(@RequestBody CadastroRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    usuarioService.cadastrarUsuario(request)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    e.getMessage()
            );
        }
    }
}
