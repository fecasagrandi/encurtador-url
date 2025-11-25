package br.com.casagrandi.encurtador.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SaudeController {

    /**
     * Endpoint raiz - evita 404/500 quando acessam a raiz do domínio
     */
    @GetMapping("/")
    public Map<String, String> root() {
        return Map.of(
            "api", "encurtador-url",
            "status", "ativa",
            "versao", "1.0.0"
        );
    }

    /**
     * Health check padrão - usado por load balancers, Docker, Kubernetes
     */
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }

    /**
     * Health check alternativo em português
     */
    @GetMapping("/api/saude")
    public Map<String, String> verificarSaude() {
        return Map.of(
            "status", "OK",
            "mensagem", "Encurtador de URL está rodando!"
        );
    }
}
