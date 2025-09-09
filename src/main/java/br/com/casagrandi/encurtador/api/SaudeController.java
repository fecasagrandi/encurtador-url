package br.com.casagrandi.encurtador.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SaudeController {

    @GetMapping("/saude")
    public String verificarSaude() {
        return "Encurtador de URL está rodando!";
    }
}
