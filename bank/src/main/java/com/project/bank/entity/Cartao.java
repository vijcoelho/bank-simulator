package com.project.bank.entity;

import com.project.bank.entity.enums.StatusCartao;
import com.project.bank.entity.enums.TipoCartao;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "cartoes")
public class Cartao {
    @Id
    private String id;
    @NotBlank
    private String cpfPortador;
    @NotBlank
    private String numeroCartao;
    @NotBlank
    private String senhaCartao;
    @NotBlank
    private StatusCartao statusCartao;
    @NotBlank
    private TipoCartao tipoCartao;
    private BigDecimal limiteCartaoCredito;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCpfPortador() {
        return cpfPortador;
    }

    public void setCpfPortador(String cpfPortador) {
        this.cpfPortador = cpfPortador;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public String getSenhaCartao() {
        return senhaCartao;
    }

    public void setSenhaCartao(String senhaCartao) {
        this.senhaCartao = senhaCartao;
    }

    public StatusCartao getStatusCartao() {
        return statusCartao;
    }

    public void setStatusCartao(StatusCartao statusCartao) {
        this.statusCartao = statusCartao;
    }

    public TipoCartao getTipoCartao() {
        return tipoCartao;
    }

    public void setTipoCartao(TipoCartao tipoCartao) {
        this.tipoCartao = tipoCartao;
    }

    public BigDecimal getLimiteCartaoCredito() {
        return limiteCartaoCredito;
    }

    public void setLimiteCartaoCredito(BigDecimal limiteCartaoCredito) {
        this.limiteCartaoCredito = limiteCartaoCredito;
    }
}
