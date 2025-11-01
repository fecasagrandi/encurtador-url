package br.com.casagrandi.encurtador.dto;

import java.time.LocalDateTime;

public class UsuarioResponse {

    private Long id;
    private String nomeUsuario;
    private String email;
    private LocalDateTime criadoEm;

    public UsuarioResponse(Long id, String nomeUsuario, String email, LocalDateTime criadoEm) {
        this.id = id;
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.criadoEm = criadoEm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}
