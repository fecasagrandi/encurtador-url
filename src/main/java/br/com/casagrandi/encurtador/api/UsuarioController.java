package br.com.casagrandi.encurtador.api;

import br.com.casagrandi.encurtador.dto.LoginRequest;
import br.com.casagrandi.encurtador.dto.UsuarioRequest;
import br.com.casagrandi.encurtador.dto.UsuarioResponse;
import br.com.casagrandi.encurtador.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioResponse> cadastrar(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse response = usuarioService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse> login(@Valid @RequestBody LoginRequest request) {
        UsuarioResponse response = usuarioService.autenticar(request.getNomeUsuario(), request.getSenha());
        return ResponseEntity.ok(response);
    }
}
