package br.com.casagrandi.encurtador.repository;

import br.com.casagrandi.encurtador.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByCodigoCurto(String codigoCurto);
}
