package com.desafio.pautas.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Assembleia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "pauta_id")
    private Pauta pauta;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataHoraInicio;

    @Column
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataHoraFim;

    @Column
    private Long tempoSessao;

}
