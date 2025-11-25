package br.com.casagrandi.encurtador.api;

import br.com.casagrandi.encurtador.dto.EncurtarRequest;
import br.com.casagrandi.encurtador.dto.EncurtarResponse;
import br.com.casagrandi.encurtador.dto.EstatisticasResponse;
import br.com.casagrandi.encurtador.dto.UrlResponse;
import br.com.casagrandi.encurtador.exception.UrlNaoEncontradaException;
import br.com.casagrandi.encurtador.model.Url;
import br.com.casagrandi.encurtador.model.Usuario;
import br.com.casagrandi.encurtador.repository.UsuarioRepository;
import br.com.casagrandi.encurtador.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
public class UrlController {

    private final UrlService service;
    private final UsuarioRepository usuarioRepository;

    @Value("${app.base-url:https://encurtador-backend.kaizenapp.com.br}")
    private String baseUrl;

    public UrlController(UrlService service, UsuarioRepository usuarioRepository) {
        this.service = service;
        this.usuarioRepository = usuarioRepository;
    }

    private Long obterUsuarioIdAutenticado(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }
        
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByNomeUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        return usuario.getId();
    }

    /**
     * Encurtar URL PÚBLICA (sem login) - URLs anônimas
     * Qualquer pessoa pode encurtar, mas não terá histórico/estatísticas
     */
    @PostMapping("/api/encurtar/publico")
    public ResponseEntity<EncurtarResponse> encurtarPublico(
            @Valid @RequestBody EncurtarRequest request
    ) {
        Url url = service.encurtarAnonimo(request.getUrlOriginal());

        String urlCurta = baseUrl + "/" + url.getCodigoCurto();
        EncurtarResponse response = new EncurtarResponse(
                urlCurta,
                url.getCodigoCurto(),
                url.getUrlOriginal()
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Encurtar URL AUTENTICADA - vinculada ao usuário logado
     * Usuário terá histórico e estatísticas
     */
    @PostMapping("/api/encurtar")
    public ResponseEntity<EncurtarResponse> encurtar(
            @Valid @RequestBody EncurtarRequest request,
            Authentication authentication
    ) {
        Long usuarioId = obterUsuarioIdAutenticado(authentication);
        Url url = service.encurtar(usuarioId, request.getUrlOriginal());

        String urlCurta = baseUrl + "/" + url.getCodigoCurto();
        EncurtarResponse response = new EncurtarResponse(
                urlCurta,
                url.getCodigoCurto(),
                url.getUrlOriginal()
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Redireciona para a URL original baseado no código curto.
     * Valida se o código não está vazio para evitar conflito com rota raiz.
     */
    @GetMapping("/{codigo}")
    public RedirectView redirecionar(@PathVariable String codigo) {
        // Validação: código não pode ser vazio ou nulo
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da URL não pode ser vazio");
        }
        
        return service.buscarPorCodigo(codigo)
                .map(url -> {
                    service.registrarAcesso(url);
                    RedirectView redirectView = new RedirectView(url.getUrlOriginal());
                    redirectView.setStatusCode(org.springframework.http.HttpStatus.FOUND); // 302
                    return redirectView;
                })
                .orElseThrow(() -> new UrlNaoEncontradaException(codigo));
    }

    @GetMapping("/api/admin/urls")
    public ResponseEntity<List<UrlResponse>> listarUrls(Authentication authentication) {
        Long usuarioId = obterUsuarioIdAutenticado(authentication);
        List<UrlResponse> urls = service.listarUrlsDoUsuario(usuarioId);
        return ResponseEntity.ok(urls);
    }

    @GetMapping("/api/admin/estatisticas")
    public ResponseEntity<EstatisticasResponse> obterEstatisticas(Authentication authentication) {
        Long usuarioId = obterUsuarioIdAutenticado(authentication);
        EstatisticasResponse stats = service.obterEstatisticas(usuarioId);
        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/api/admin/urls/{id}")
    public ResponseEntity<Void> deletarUrl(@PathVariable Long id, Authentication authentication) {
        Long usuarioId = obterUsuarioIdAutenticado(authentication);
        service.deletarUrl(id, usuarioId);
        return ResponseEntity.noContent().build();
    }
}
