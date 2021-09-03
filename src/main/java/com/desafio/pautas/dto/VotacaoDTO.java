package com.desafio.pautas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotacaoDTO {
    @NotNull
    private Long associadoId;
    @NotNull
    private Long assembleiaId;
    @NotNull
    private String voto;
}
