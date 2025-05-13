package com.project.bank.exception;

public class UsuarioNaoEncotradoPeloEmailException extends RuntimeException {
    public UsuarioNaoEncotradoPeloEmailException() {
        super("Usuario nao foi encontrado pelo email passado! Tente novamente.");
    }
}
