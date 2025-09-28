package br.com.casagrandi.encurtador.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "urls")
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String urlOriginal;

    @Column(nullable = false, unique = true, length = 10)
    private String codigoCurto;

    @Column(nullable = false)
    private Long usuarioId;

    private LocalDateTime criadoEm = LocalDateTime.now();

    private Long acessos = 0L;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUrlOriginal() { return urlOriginal; }
    public void setUrlOriginal(String urlOriginal) { this.urlOriginal = urlOriginal; }

    public String getCodigoCurto() { return codigoCurto; }
    public void setCodigoCurto(String codigoCurto) { this.codigoCurto = codigoCurto; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

    public Long getAcessos() { return acessos; }
    public void setAcessos(Long acessos) { this.acessos = acessos; }
}
