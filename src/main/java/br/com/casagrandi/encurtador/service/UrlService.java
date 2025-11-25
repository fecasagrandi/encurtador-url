package br.com.casagrandi.encurtador.service;

import br.com.casagrandi.encurtador.dto.EstatisticasResponse;
import br.com.casagrandi.encurtador.dto.UrlResponse;
import br.com.casagrandi.encurtador.model.Url;
import br.com.casagrandi.encurtador.model.Usuario;
import br.com.casagrandi.encurtador.repository.UrlRepository;
import br.com.casagrandi.encurtador.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final UsuarioRepository usuarioRepository;
    private final Random random = new Random();

    // baseUrl removido do service - será passado como parâmetro

    public UrlService(UrlRepository urlRepository, UsuarioRepository usuarioRepository) {
        this.urlRepository = urlRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Encurta URL para usuário autenticado (com histórico)
     */
    public Url encurtar(Long usuarioId, String urlOriginal) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Url url = new Url();
        url.setUsuario(usuario);
        url.setUrlOriginal(urlOriginal);
        url.setCodigoCurto(gerarCodigo());
        url.setAcessos(0L);

        return urlRepository.save(url);
    }

    /**
     * Encurta URL anonimamente (sem usuário vinculado)
     * Funciona, mas não aparece no histórico de ninguém
     */
    public Url encurtarAnonimo(String urlOriginal) {
        Url url = new Url();
        url.setUsuario(null); // URL anônima
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
        url.setUltimoAcesso(LocalDateTime.now());
        urlRepository.save(url);
    }

    public List<UrlResponse> listarUrlsDoUsuario(Long usuarioId, String baseUrl) {
        List<Url> urls = urlRepository.findByUsuarioIdOrderByCriadoEmDesc(usuarioId);
        return urls.stream()
                .map(url -> converterParaResponse(url, baseUrl))
                .collect(Collectors.toList());
    }

    public EstatisticasResponse obterEstatisticas(Long usuarioId) {
        List<Url> urls = urlRepository.findByUsuarioIdOrderByCriadoEmDesc(usuarioId);
        Long totalUrls = (long) urls.size();
        Long totalAcessos = urlRepository.somarAcessosPorUsuario(usuarioId);
        
        if (totalAcessos == null) {
            totalAcessos = 0L;
        }

        Optional<Url> urlMaisAcessada = urlRepository.findFirstByUsuarioIdOrderByAcessosDesc(usuarioId);
        Long urlMaisAcessadaId = urlMaisAcessada.map(Url::getId).orElse(null);
        Long acessosMaisAcessada = urlMaisAcessada.map(Url::getAcessos).orElse(0L);

        return new EstatisticasResponse(totalUrls, totalAcessos, urlMaisAcessadaId, acessosMaisAcessada);
    }

    public void deletarUrl(Long id, Long usuarioId) {
        Url url = urlRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("URL não encontrada"));
        
        if (!url.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Você não tem permissão para deletar esta URL");
        }
        
        urlRepository.delete(url);
    }

    private UrlResponse converterParaResponse(Url url, String baseUrl) {
        String urlCurta = baseUrl + "/" + url.getCodigoCurto();
        return new UrlResponse(
                url.getId(),
                url.getUrlOriginal(),
                url.getCodigoCurto(),
                urlCurta,
                url.getAcessos(),
                url.getCriadoEm()
        );
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
