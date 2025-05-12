package com.project.bank.mapper;

import com.project.bank.dto.usuario.request.CadastroRequest;
import com.project.bank.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public Usuario toEntity(CadastroRequest request) {
        if (request == null) return null;

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setCpf(request.getCpf());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(request.getSenha());
        usuario.setIdade(request.getIdade());

        return usuario;
    }
}
