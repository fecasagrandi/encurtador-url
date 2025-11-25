package br.com.casagrandi.encurtador.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Origens permitidas
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",
            "https://encurtador.kaizenapp.com.br",
            "https://encurtador-frontend.kaizenapp.com.br"
        ));
        
        // Métodos permitidos (incluindo OPTIONS para preflight)
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"
        ));
        
        // Headers permitidos
        configuration.setAllowedHeaders(List.of("*"));
        
        // Permite enviar credenciais (cookies, Authorization header)
        configuration.setAllowCredentials(true);
        
        // Headers expostos na resposta
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization", "Content-Type", "X-Requested-With"
        ));
        
        // Cache do preflight (1 hora)
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
