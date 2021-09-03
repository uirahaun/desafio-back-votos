package com.desafio.pautas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PautaDTO {
    @NotNull
    private Long id;
    @NotNull
    private String tema;
    @NotNull
    private String conteudo;
}
