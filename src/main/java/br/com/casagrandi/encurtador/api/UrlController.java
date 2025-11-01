package br.com.casagrandi.encurtador.api;

import br.com.casagrandi.encurtador.dto.EncurtarRequest;
import br.com.casagrandi.encurtador.dto.EncurtarResponse;
import br.com.casagrandi.encurtador.dto.EstatisticasResponse;
import br.com.casagrandi.encurtador.dto.UrlResponse;
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

    @Value("${app.base-url:http://localhost:8080}")
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

    @GetMapping("/{codigo}")
    public RedirectView redirecionar(@PathVariable String codigo) {
        return service.buscarPorCodigo(codigo)
                .map(url -> {
                    service.registrarAcesso(url);
                    return new RedirectView(url.getUrlOriginal());
                })
                .orElseThrow(() -> new RuntimeException("Código não encontrado"));
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
