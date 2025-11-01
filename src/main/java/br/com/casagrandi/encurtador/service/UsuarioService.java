package br.com.casagrandi.encurtador.service;

import br.com.casagrandi.encurtador.dto.UsuarioRequest;
import br.com.casagrandi.encurtador.dto.UsuarioResponse;
import br.com.casagrandi.encurtador.model.Usuario;
import br.com.casagrandi.encurtador.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponse cadastrar(UsuarioRequest request) {
        if (usuarioRepository.existsByNomeUsuario(request.getNomeUsuario())) {
            throw new RuntimeException("Nome de usuário já existe");
        }

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNomeUsuario(request.getNomeUsuario());
        usuario.setEmail(request.getEmail());
        usuario.setSenhaHash(passwordEncoder.encode(request.getSenha()));

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return new UsuarioResponse(
                usuarioSalvo.getId(),
                usuarioSalvo.getNomeUsuario(),
                usuarioSalvo.getEmail(),
                usuarioSalvo.getCriadoEm()
        );
    }

    public UsuarioResponse autenticar(String nomeUsuario, String senha) {
        Usuario usuario = usuarioRepository.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário ou senha inválidos"));

        if (!passwordEncoder.matches(senha, usuario.getSenhaHash())) {
            throw new RuntimeException("Usuário ou senha inválidos");
        }

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNomeUsuario(),
                usuario.getEmail(),
                usuario.getCriadoEm()
        );
    }
}
