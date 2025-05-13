package com.project.bank.dto.usuario.response;

public class LoginResponse {
    private String cpf;
    private String mensagem;
    private String token;

    public LoginResponse(String cpf, String mensagem, String token) {
        this.cpf = cpf;
        this.mensagem = mensagem;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
