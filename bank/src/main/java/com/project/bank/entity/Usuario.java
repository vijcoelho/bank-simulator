package com.project.bank.entity;

import com.project.bank.entity.enums.StatusUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Document(collection = "usuarios")
public class Usuario implements UserDetails {
    @Id
    private String id;
    @NotBlank
    private String nome;
    @NotBlank
    @CPF
    @Indexed(unique = true)
    private String cpf;
    @NotBlank
    @Indexed(unique = true)
    private String email;
    @NotBlank
    private String senha;
    @NotNull
    private Integer idade;
    private BigDecimal saldo = BigDecimal.ZERO;
    private StatusUsuario statusDoUsuario;
    private List<Cartao> cartoes = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public StatusUsuario getStatusDoUsuario() {
        return statusDoUsuario;
    }

    public void setStatusDoUsuario(StatusUsuario statusDoUsuario) {
        this.statusDoUsuario = statusDoUsuario;
    }

    public List<Cartao> getCartoes() {
        return cartoes;
    }

    public void setCartoes(List<Cartao> cartoes) {
        this.cartoes = cartoes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
