package br.com.casagrandi.encurtador.dto;

public class EstatisticasResponse {

    private Long totalUrls;
    private Long totalAcessos;
    private Long urlMaisAcessada;
    private Long acessosMaisAcessada;

    public EstatisticasResponse(Long totalUrls, Long totalAcessos, Long urlMaisAcessada, Long acessosMaisAcessada) {
        this.totalUrls = totalUrls;
        this.totalAcessos = totalAcessos;
        this.urlMaisAcessada = urlMaisAcessada;
        this.acessosMaisAcessada = acessosMaisAcessada;
    }

    public Long getTotalUrls() {
        return totalUrls;
    }

    public void setTotalUrls(Long totalUrls) {
        this.totalUrls = totalUrls;
    }

    public Long getTotalAcessos() {
        return totalAcessos;
    }

    public void setTotalAcessos(Long totalAcessos) {
        this.totalAcessos = totalAcessos;
    }

    public Long getUrlMaisAcessada() {
        return urlMaisAcessada;
    }

    public void setUrlMaisAcessada(Long urlMaisAcessada) {
        this.urlMaisAcessada = urlMaisAcessada;
    }

    public Long getAcessosMaisAcessada() {
        return acessosMaisAcessada;
    }

    public void setAcessosMaisAcessada(Long acessosMaisAcessada) {
        this.acessosMaisAcessada = acessosMaisAcessada;
    }
}
