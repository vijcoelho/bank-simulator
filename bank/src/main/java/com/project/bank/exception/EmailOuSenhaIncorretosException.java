package com.project.bank.exception;

public class EmailOuSenhaIncorretosException extends RuntimeException {
    public EmailOuSenhaIncorretosException() {
        super("Email ou senha estao incorretos! Tente Novamente.");
    }
}
