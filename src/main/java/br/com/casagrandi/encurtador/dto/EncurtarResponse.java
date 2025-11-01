package br.com.casagrandi.encurtador.dto;

public class EncurtarResponse {

    private String urlCurta;
    private String codigoCurto;
    private String urlOriginal;

    public EncurtarResponse(String urlCurta, String codigoCurto, String urlOriginal) {
        this.urlCurta = urlCurta;
        this.codigoCurto = codigoCurto;
        this.urlOriginal = urlOriginal;
    }

    public String getUrlCurta() {
        return urlCurta;
    }

    public void setUrlCurta(String urlCurta) {
        this.urlCurta = urlCurta;
    }

    public String getCodigoCurto() {
        return codigoCurto;
    }

    public void setCodigoCurto(String codigoCurto) {
        this.codigoCurto = codigoCurto;
    }

    public String getUrlOriginal() {
        return urlOriginal;
    }

    public void setUrlOriginal(String urlOriginal) {
        this.urlOriginal = urlOriginal;
    }
}
