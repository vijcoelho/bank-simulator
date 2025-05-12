package com.project.bank.exception;

public class UsuarioNaoPossuiIdadeException extends RuntimeException {
    public UsuarioNaoPossuiIdadeException() {
        super("Usuario nao possui idade minima de 18 para realizar o cadastro!");
    }
}
