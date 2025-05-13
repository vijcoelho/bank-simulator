package com.project.bank.dto.usuario.request;

public class GerarTokenSenhaRquest {
    private String email;

    public GerarTokenSenhaRquest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
