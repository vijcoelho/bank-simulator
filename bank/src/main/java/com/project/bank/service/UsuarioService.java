package com.project.bank.service;

import com.project.bank.dto.usuario.request.*;
import com.project.bank.dto.usuario.response.GerarTokenSenhaResponse;
import com.project.bank.dto.usuario.response.LoginResponse;
import com.project.bank.dto.usuario.response.ResponsePadrao;
import com.project.bank.entity.suporte.TokenSenha;
import com.project.bank.entity.Usuario;
import com.project.bank.entity.enums.StatusUsuario;
import com.project.bank.exception.CpfJaCadastradoException;
import com.project.bank.exception.EmailOuSenhaIncorretosException;
import com.project.bank.exception.UsuarioNaoEncotradoPeloEmailException;
import com.project.bank.exception.UsuarioNaoPossuiIdadeException;
import com.project.bank.mapper.UsuarioMapper;
import com.project.bank.repository.UsuarioRepository;
import com.project.bank.security.jwt.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    private Map<String, TokenSenha> tokenSenha = new ConcurrentHashMap<>();
    private Usuario usuarioLogado;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, BCryptPasswordEncoder passwordEncoder, JwtService jwtService, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
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
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuarioRepository.save(usuario);

        try {
            String corpo = """
                Olá %s,
    
                🎉 Seja muito bem-vindo(a) ao Bank-Simulator!
    
                Sua conta foi criada com sucesso e você já pode acessar nossos serviços bancários simulados com segurança e praticidade.
    
                Aqui estão alguns próximos passos:
                - Acesse sua conta com suas credenciais cadastradas.
                - Explore funcionalidades como saldo, transferências e depósitos.
                - Em caso de dúvidas, nossa equipe de suporte está à disposição.
    
                Obrigado por escolher o Bank-Simulator! 💼
                Estamos felizes em ter você conosco.
    
                Atenciosamente,
                        Equipe Bank-Simulator
                """.formatted(usuario.getNome());
                    emailService.enviarEmail(
                            usuario.getEmail(),
                            "Conta criada com sucesso no Bank-Simulator!",
                            corpo
                    );
        } catch (Exception e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
            e.printStackTrace();
        }

        return new ResponsePadrao(
                usuario.getCpf(),
                "Usuario cadastrado com sucesso!"
        );
    }

    public LoginResponse login(LoginRequest request) {
        if (request == null) throw new RuntimeException("Request esta nulo");

        Usuario usuario = usuarioRepository.findUsuarioByEmail(request.getEmail());
        if (usuario == null) {
            throw new UsuarioNaoEncotradoPeloEmailException();
        }
        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new EmailOuSenhaIncorretosException();
        }

        String token = jwtService.generateToken(usuario);
        usuarioLogado = usuario;
        return new LoginResponse(
                usuario.getCpf(),
                "Login realizado com sucesso!",
                token
        );
    }

    public Usuario getUsuarioLogado(String jwtToken) {
        String jwtTokenFormatado = jwtToken.replace("Bearer ", "");
        String jwtEmail = jwtService.extractEmail(jwtTokenFormatado);
        Usuario usuario = usuarioRepository.findUsuarioByEmail(jwtEmail);

        if (usuario != null && !usuario.getEmail().equals(jwtEmail)) {
            throw new RuntimeException("Email ou Token incorretos! Tente Novamente.");
        }
        return this.usuarioLogado;
    }

    public GerarTokenSenhaResponse gerarTokenSenha(GerarTokenSenhaRquest request, String jwtToken) {
        String jwtTokenFormatado = jwtToken.replace("Bearer ", "");
        String jwtEmail = jwtService.extractEmail(jwtTokenFormatado);
        Usuario usuario = usuarioRepository.findUsuarioByEmail(request.getEmail());

        if (!usuario.getEmail().equals(jwtEmail)) {
            throw new RuntimeException("Email ou Token incorretos! Tente Novamente.");
        }

        limparTokensExpirados();
        String token = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        tokenSenha.put(usuario.getEmail(), new TokenSenha(token, LocalDateTime.now().plusMinutes(10)));

        try {
            String corpo = """
                Olá %s,
                
                🔐 Recebemos uma solicitação para redefinir sua senha no Bank-Simulator.
                
                Se foi você quem solicitou, utilize o token abaixo para continuar o processo de redefinição com segurança:
                
                👉 Token de verificação: **%s**
                
                Este token é válido por tempo limitado. Não compartilhe com ninguém.
                
                Se você não solicitou essa alteração, por favor ignore este e-mail. Sua conta permanece segura.
              
                Atenciosamente,  
                Equipe Bank-Simulator
                """.formatted(usuario.getNome(), token);
            emailService.enviarEmail(
                    usuario.getEmail(),
                    "Redefinir sua senha!",
                    corpo
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new GerarTokenSenhaResponse(
                usuario.getEmail(),
                token
        );
    }

    public ResponsePadrao trocarSenha(TrocarSenhaRequest request, String jwtToken) {
        String jwtTokenFormatado = jwtToken.replace("Bearer ", "");
        String jwtEmail = jwtService.extractEmail(jwtTokenFormatado);
        Usuario usuario = usuarioRepository.findUsuarioByEmail(request.getEmail());

        TokenSenha tokenInfos = tokenSenha.get(request.getEmail());
        if (tokenInfos == null || tokenInfos.getDataExpiracao().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado ou invalido! Gere um novo token.");
        }
        if (!tokenInfos.getToken().equals(request.getTokenSenha())) {
            throw new RuntimeException("Token invalido! Cheque seu email para ver o token");
        }

        if (!usuario.getEmail().equals(jwtEmail)) {
            throw new RuntimeException("Email ou Token incorretos! Tente Novamente.");
        }
        if (!request.getSenha().equals(request.getConfirmarSenha())) {
            throw new RuntimeException("Nova senha nao pode ser diferente da confirmada!");
        }
        if (passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Nova senha nao pode ser igual a senha atual!");
        }

        tokenSenha.remove(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getConfirmarSenha()));
        LocalDateTime data = LocalDateTime.now();

        try {
            String corpo = """
                Olá %s,
    
                🔐 Sua senha foi alterada com sucesso!
        
                Informamos que a alteração da sua senha no Bank-Simulator foi realizada com êxito em %s.
                Se você fez essa alteração, não é necessário fazer mais nada.
        
                ⚠️ Caso não reconheça essa ação, recomendamos que entre em contato imediatamente com nossa equipe de suporte e redefina sua senha por segurança.
        
                Obrigado por utilizar o Bank-Simulator!
        
                Atenciosamente,
                Equipe Bank-Simulator
                """.formatted(usuario.getNome(), data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")));
            emailService.enviarEmail(
                    usuario.getEmail(),
                    "Senha alterada com sucesso!",
                    corpo
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        usuarioRepository.save(usuario);
        return new ResponsePadrao(
                usuario.getCpf(),
                "Senha alterada com sucesso, cheque seu email!"
        );
    }

    private void limparTokensExpirados() {
        LocalDateTime agora = LocalDateTime.now();
        tokenSenha.entrySet().removeIf(entry -> entry.getValue().getDataExpiracao().isBefore(agora));
    }

    public ResponsePadrao depositar(DepositoRequest request, String jwtToken) {
        String jwtTokenFormatado = jwtToken.replace("Bearer ", "");
        String jwtEmail = jwtService.extractEmail(jwtTokenFormatado);
        Usuario usuario = usuarioRepository.findUsuarioByEmail(jwtEmail);

        if (usuario == null) {
            throw new UsuarioNaoEncotradoPeloEmailException();
        }
        if (!usuario.getEmail().equals(jwtEmail)) {
            throw new RuntimeException("Email ou Token incorretos! Tente Novamente.");
        }
        if(!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            return new ResponsePadrao(
                    usuario.getCpf(),
                    "Senha incorreta!"
            );
        }

        usuario.setSaldo(usuario.getSaldo().add(request.getValor()));
        usuarioRepository.save(usuario);

        return new ResponsePadrao(
                usuario.getCpf(),
                "Valor depositado com sucesso! Total: " + usuario.getSaldo()
        );
    }

    public ResponsePadrao sacar(SaqueRequest request, String jwtToken) {
        String jwtTokenFormatado = jwtToken.replace("Bearer ", "");
        String jwtEmail = jwtService.extractEmail(jwtTokenFormatado);
        Usuario usuario = usuarioRepository.findUsuarioByEmail(jwtEmail);

        if (usuario == null) {
            throw new UsuarioNaoEncotradoPeloEmailException();
        }
        if (!usuario.getEmail().equals(jwtEmail)) {
            throw new RuntimeException("Email ou Token incorretos! Tente Novamente.");
        }

        if(!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            return new ResponsePadrao(
                    usuario.getCpf(),
                    "Senha incorreta!"
            );
        }
        if (usuario.getSaldo().compareTo(request.getValor()) < 0) {
            return new ResponsePadrao(
                    usuario.getCpf(),
                    "Nao possui saldo suficiente para saque! Saldo: " + usuario.getSaldo()
            );
        }

        usuario.setSaldo(usuario.getSaldo().subtract(request.getValor()));
        usuarioRepository.save(usuario);

        return new ResponsePadrao(
                usuario.getCpf(),
                "Saque realizado com sucesso! Total: " + usuario.getSaldo()
        );
    }
}
