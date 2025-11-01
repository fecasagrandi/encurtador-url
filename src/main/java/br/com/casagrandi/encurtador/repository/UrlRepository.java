package br.com.casagrandi.encurtador.repository;

import br.com.casagrandi.encurtador.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByCodigoCurto(String codigoCurto);
    
    List<Url> findByUsuarioIdOrderByCriadoEmDesc(Long usuarioId);
    
    @Query("SELECT SUM(u.acessos) FROM Url u WHERE u.usuario.id = :usuarioId")
    Long somarAcessosPorUsuario(Long usuarioId);
    
    Optional<Url> findFirstByUsuarioIdOrderByAcessosDesc(Long usuarioId);
}
