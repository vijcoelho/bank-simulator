package com.project.bank.dto.usuario.request;

public class TrocarSenhaRequest {
    private String email;
    private String senha;
    private String confirmarSenha;
    private String tokenSenha;

    public TrocarSenhaRequest(String email, String senha, String confirmarSenha, String tokenSenha) {
        this.email = email;
        this.senha = senha;
        this.confirmarSenha = confirmarSenha;
        this.tokenSenha = tokenSenha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }

    public String getTokenSenha() {
        return tokenSenha;
    }

    public void setTokenSenha(String tokenSenha) {
        this.tokenSenha = tokenSenha;
    }
}
