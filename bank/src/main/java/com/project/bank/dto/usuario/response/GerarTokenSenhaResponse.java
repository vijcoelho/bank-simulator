package com.project.bank.dto.usuario.response;

public class GerarTokenSenhaResponse {
    private String email;
    private String tokenSenha;

    public GerarTokenSenhaResponse(String email, String tokenSenha) {
        this.email = email;
        this.tokenSenha = tokenSenha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTokenSenha() {
        return tokenSenha;
    }

    public void setTokenSenha(String tokenSenha) {
        this.tokenSenha = tokenSenha;
    }
}
