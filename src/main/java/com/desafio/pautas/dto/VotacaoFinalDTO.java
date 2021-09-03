package com.desafio.pautas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotacaoFinalDTO {
    private Long totalVotos;
    private Long totalSim;
    private Long totalNao;
    private AssembleiaDTO assembleia;
    private PautaDTO pautaDTO;
}
