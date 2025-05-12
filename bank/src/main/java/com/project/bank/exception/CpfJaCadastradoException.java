package com.project.bank.exception;

public class CpfJaCadastradoException extends RuntimeException {
    public CpfJaCadastradoException() {
        super("CPF ja cadastrado, tente novamente um valido!");
    }
}
