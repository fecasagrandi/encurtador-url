package br.com.casagrandi.encurtador.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "acessos_urls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcessoUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "url_id", nullable = false)
    private Url url;

    @Column(name = "acessado_em", nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime acessadoEm;

    @Column(length = 45)
    private String ip;

    @Column(length = 255)
    private String userAgent;
}
