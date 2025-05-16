package com.project.bank.dto.usuario.request;

import java.math.BigDecimal;

public class DepositoRequest {
    private String email;
    private String senha;
    private BigDecimal valor;

    public DepositoRequest(String email, String senha, BigDecimal valor) {
        this.email = email;
        this.senha = senha;
        this.valor = valor;
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

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}

