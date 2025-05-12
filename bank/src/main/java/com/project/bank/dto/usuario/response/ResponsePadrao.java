package com.project.bank.dto.usuario.response;

public class ResponsePadrao {
    private String cpf;
    private String mensagem;

    public ResponsePadrao(String cpf, String mensagem) {
        this.cpf = cpf;
        this.mensagem = mensagem;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
