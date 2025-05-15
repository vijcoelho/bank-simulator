package com.project.bank.entity.suporte;

import java.time.LocalDateTime;

public class TokenSenha {
    private final String token;
    private final LocalDateTime dataExpiracao;

    public TokenSenha(String token, LocalDateTime dataExpiracao) {
        this.token = token;
        this.dataExpiracao = dataExpiracao;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }
}
