package com.project.bank.controller;

import com.project.bank.dto.usuario.request.*;
import com.project.bank.dto.usuario.response.ResponsePadrao;
import com.project.bank.entity.Usuario;
import com.project.bank.repository.UsuarioRepository;
import com.project.bank.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioService usuarioService, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
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

    @PostMapping("/logar")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    usuarioService.login(request)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    e.getMessage()
            );
        }
    }

    @PostMapping("/gerar-token-senha")
    public ResponseEntity<?> gerarToken(@RequestBody GerarTokenSenhaRquest request, @RequestHeader("Authorization") String jwtToken) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    usuarioService.gerarTokenSenha(request, jwtToken)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    e.getMessage()
            );
        }
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity<?> gerarToken(@RequestBody TrocarSenhaRequest request, @RequestHeader("Authorization") String jwtToken) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    usuarioService.trocarSenha(request, jwtToken)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    e.getMessage()
            );
        }
    }

    @GetMapping("/pegar-usuario-logado")
    public ResponseEntity<Usuario> getUsuarioLogado(@RequestHeader("Authorization") String jwtToken) {
        return ResponseEntity.status(200).body(usuarioService.getUsuarioLogado(jwtToken));
    }

    @PutMapping("/saldo/deposito")
    public ResponseEntity<?> depostio(@RequestBody DepositoRequest request, @RequestHeader("Authorization") String jwtToken) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    usuarioService.depositar(request, jwtToken)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    e.getMessage()
            );
        }
    }

    @PutMapping("/saldo/saque")
    public ResponseEntity<?> saque(@RequestBody SaqueRequest request, @RequestHeader("Authorization") String jwtToken) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    usuarioService.sacar(request, jwtToken)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    e.getMessage()
            );
        }
    }
}
