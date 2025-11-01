package br.com.casagrandi.encurtador.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class EncurtarRequest {

    @NotBlank(message = "URL não pode estar vazia")
    @Pattern(regexp = "^https?://.*", message = "URL deve começar com http:// ou https://")
    private String urlOriginal;

    public String getUrlOriginal() {
        return urlOriginal;
    }

    public void setUrlOriginal(String urlOriginal) {
        this.urlOriginal = urlOriginal;
    }
}
