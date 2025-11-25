package br.com.casagrandi.encurtador.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // CORS para todas as rotas (API, health, redirecionamento)
        registry.addMapping("/**")
                .allowedOrigins(
                    "http://localhost:3000",
                    "https://encurtador.kaizenapp.com.br",
                    "https://encurtador-frontend.kaizenapp.com.br"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
