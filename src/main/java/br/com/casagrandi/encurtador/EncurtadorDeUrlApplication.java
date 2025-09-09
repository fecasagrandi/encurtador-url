package br.com.casagrandi.encurtador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class EncurtadorDeUrlApplication {
    public static void main(String[] args) {
        SpringApplication.run(EncurtadorDeUrlApplication.class, args);
    }
}
