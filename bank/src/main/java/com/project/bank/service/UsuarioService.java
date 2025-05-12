package com.project.bank.service;

import com.project.bank.dto.usuario.request.CadastroRequest;
import com.project.bank.dto.usuario.response.ResponsePadrao;
import com.project.bank.entity.Usuario;
import com.project.bank.entity.enums.StatusUsuario;
import com.project.bank.exception.CpfJaCadastradoException;
import com.project.bank.exception.UsuarioNaoPossuiIdadeException;
import com.project.bank.mapper.UsuarioMapper;
import com.project.bank.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    public ResponsePadrao cadastrarUsuario(CadastroRequest request) {
        if (request == null) throw new RuntimeException("Request esta nulo");

        Usuario usuario = usuarioMapper.toEntity(request);
        Usuario usuarioProcurar = usuarioRepository.findUsuarioByCpf(request.getCpf());
        if (usuarioProcurar != null) {
            if (usuarioProcurar.getCpf().equals(usuario.getCpf())) {
                throw new CpfJaCadastradoException();
            }
        }

        if (usuario.getIdade() < 18) {
            throw new UsuarioNaoPossuiIdadeException();
        }

        usuario.setStatusDoUsuario(StatusUsuario.ATIVO);
        usuarioRepository.save(usuario);
        return new ResponsePadrao(
                usuario.getCpf(),
                "Usuario cadastrado com sucesso!"
        );
    }
}
