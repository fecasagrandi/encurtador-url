package br.com.casagrandi.encurtador.api;

import br.com.casagrandi.encurtador.model.Url;
import br.com.casagrandi.encurtador.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UrlController {

    private final UrlService service;

    public UrlController(UrlService service) {
        this.service = service;
    }

    @PostMapping("/encurtar")
    public ResponseEntity<Map<String, String>> encurtar(@RequestBody Map<String, String> body) {
        Long usuarioId = 1L; // temporário até integrar usuários
        String urlOriginal = body.get("urlOriginal");
        Url url = service.encurtar(usuarioId, urlOriginal);

        Map<String, String> resposta = new HashMap<>();
        resposta.put("urlCurta", "http://localhost:8080/" + url.getCodigoCurto());

        return ResponseEntity.ok(resposta);
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
}
