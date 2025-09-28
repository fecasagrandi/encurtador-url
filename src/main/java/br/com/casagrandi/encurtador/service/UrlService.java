package br.com.casagrandi.encurtador.service;

import br.com.casagrandi.encurtador.model.Url;
import br.com.casagrandi.encurtador.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UrlService {

    private final UrlRepository repository;
    private final Random random = new Random();

    public UrlService(UrlRepository repository) {
        this.repository = repository;
    }

    public Url encurtar(Long usuarioId, String urlOriginal) {
        Url url = new Url();
        url.setUsuarioId(usuarioId);
        url.setUrlOriginal(urlOriginal);
        url.setCodigoCurto(gerarCodigo());
        return repository.save(url);
    }

    public Optional<Url> buscarPorCodigo(String codigo) {
        return repository.findByCodigoCurto(codigo);
    }

    public void registrarAcesso(Url url) {
        url.setAcessos(url.getAcessos() + 1);
        repository.save(url);
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
