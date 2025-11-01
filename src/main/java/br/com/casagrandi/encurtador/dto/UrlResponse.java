package br.com.casagrandi.encurtador.dto;

import java.time.LocalDateTime;

public class UrlResponse {

    private Long id;
    private String urlOriginal;
    private String codigoCurto;
    private String urlCurta;
    private Long acessos;
    private LocalDateTime criadoEm;

    public UrlResponse(Long id, String urlOriginal, String codigoCurto, String urlCurta, Long acessos, LocalDateTime criadoEm) {
        this.id = id;
        this.urlOriginal = urlOriginal;
        this.codigoCurto = codigoCurto;
        this.urlCurta = urlCurta;
        this.acessos = acessos;
        this.criadoEm = criadoEm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlOriginal() {
        return urlOriginal;
    }

    public void setUrlOriginal(String urlOriginal) {
        this.urlOriginal = urlOriginal;
    }

    public String getCodigoCurto() {
        return codigoCurto;
    }

    public void setCodigoCurto(String codigoCurto) {
        this.codigoCurto = codigoCurto;
    }

    public String getUrlCurta() {
        return urlCurta;
    }

    public void setUrlCurta(String urlCurta) {
        this.urlCurta = urlCurta;
    }

    public Long getAcessos() {
        return acessos;
    }

    public void setAcessos(Long acessos) {
        this.acessos = acessos;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}
