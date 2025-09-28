package br.com.casagrandi.encurtador.service;

import br.com.casagrandi.encurtador.model.Url;
import br.com.casagrandi.encurtador.model.Usuario;
import br.com.casagrandi.encurtador.repository.UrlRepository;
import br.com.casagrandi.encurtador.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final UsuarioRepository usuarioRepository;
    private final Random random = new Random();

    public UrlService(UrlRepository urlRepository, UsuarioRepository usuarioRepository) {
        this.urlRepository = urlRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Url encurtar(Long usuarioId, String urlOriginal) {
        // Busca o usuário pelo ID
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        Url url = new Url();
        url.setUsuario(usuario);
        url.setUrlOriginal(urlOriginal);
        url.setCodigoCurto(gerarCodigo());
        url.setAcessos(0L);

        return urlRepository.save(url);
    }

    public Optional<Url> buscarPorCodigo(String codigo) {
        return urlRepository.findByCodigoCurto(codigo);
    }

    public void registrarAcesso(Url url) {
        url.setAcessos(url.getAcessos() + 1);
        urlRepository.save(url);
    }

    private String gerarCodigo() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
