package br.com.casagrandi.encurtador.api;

import br.com.casagrandi.encurtador.dto.EncurtarRequest;
import br.com.casagrandi.encurtador.dto.EncurtarResponse;
import br.com.casagrandi.encurtador.dto.EstatisticasResponse;
import br.com.casagrandi.encurtador.dto.UrlResponse;
import br.com.casagrandi.encurtador.model.Url;
import br.com.casagrandi.encurtador.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
public class UrlController {

    private final UrlService service;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public UrlController(UrlService service) {
        this.service = service;
    }

    @PostMapping("/api/encurtar")
    public ResponseEntity<EncurtarResponse> encurtar(@Valid @RequestBody EncurtarRequest request) {
        Long usuarioId = 1L;
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
    public ResponseEntity<List<UrlResponse>> listarUrls() {
        Long usuarioId = 1L;
        List<UrlResponse> urls = service.listarUrlsDoUsuario(usuarioId);
        return ResponseEntity.ok(urls);
    }

    @GetMapping("/api/admin/estatisticas")
    public ResponseEntity<EstatisticasResponse> obterEstatisticas() {
        Long usuarioId = 1L;
        EstatisticasResponse stats = service.obterEstatisticas(usuarioId);
        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/api/admin/urls/{id}")
    public ResponseEntity<Void> deletarUrl(@PathVariable Long id) {
        Long usuarioId = 1L;
        service.deletarUrl(id, usuarioId);
        return ResponseEntity.noContent().build();
    }
}
