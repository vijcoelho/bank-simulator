package com.project.bank.repository;

import com.project.bank.entity.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Usuario findUsuarioByCpf(String cpf);
    Usuario findUsuarioByEmail(String email);
}
