package br.com.casagrandi.encurtador.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SaudeController {

    @GetMapping("/api/saude")
    public Map<String, String> verificarSaude() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("mensagem", "Encurtador de URL está rodando!");
        return response;
    }
}
