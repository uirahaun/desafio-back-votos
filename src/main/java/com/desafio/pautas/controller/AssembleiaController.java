package com.desafio.pautas.controller;

import com.desafio.pautas.dto.AssembleiaDTO;
import com.desafio.pautas.entity.Assembleia;
import com.desafio.pautas.entity.Associado;
import com.desafio.pautas.repository.AssembleiaRepository;
import com.desafio.pautas.repository.PautaRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/assembleia")
public class AssembleiaController {

    private AssembleiaRepository assembleiaRepository;

    @Autowired
    private PautaRepository pautaRepository;

    AssembleiaController(AssembleiaRepository assembleiaRepository) {
        this.assembleiaRepository = assembleiaRepository;
    }

    @GetMapping("/{assembleiaId}")
    @ApiOperation(value = "Find Assembleia by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Assembleia Returned Successfully"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public Assembleia findById(@ApiParam(value = "Request Assembleia by ID", required = true)
                               @Valid @PathVariable Long assembleiaId) {
        return this.assembleiaRepository.findById(assembleiaId).orElseThrow(() -> new EntityNotFoundException("Assembleia não encontrada."));
    }

    @ApiOperation(value = "Iniciar assembléia.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Iniciada com sucesso"),
            @ApiResponse(code = 401, message = "Você não tem autorização de acesso."),
            @ApiResponse(code = 404, message = "Erro no processamento.")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Assembleia startAssembleia(@ApiParam(value = "Assembléia object que será iniciada.")
                                      @Valid @RequestBody AssembleiaDTO assembleia) {
        Assembleia newAssembleia = new Assembleia();
        newAssembleia.setPauta(pautaRepository.getById(assembleia.getPautaId()));
        newAssembleia.setTempoSessao(assembleia.getTempoSessao());

        Associado ass = new Associado();

        LocalDateTime dataHoraFim = LocalDateTime.now();

        if (assembleia.getDataHoraInicio().isBefore(LocalDateTime.now())) {
            throw new EntityNotFoundException("A data e hora deve ser mais atual.");
        }

        if (assembleia.getTempoSessao() != null) {
            dataHoraFim = assembleia.getDataHoraInicio().plusMinutes(assembleia.getTempoSessao());
        } else {
            dataHoraFim = dataHoraFim.plusMinutes(1L);
        }

        newAssembleia.setDataHoraInicio(assembleia.getDataHoraInicio());
        newAssembleia.setDataHoraFim(dataHoraFim);

        return this.assembleiaRepository.save(newAssembleia);
    }

    @GetMapping("/findAll")
    @ApiOperation(value = "Find All Assembleia")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Assembleias Returned Successfully"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<List<Assembleia>> findAll() {
        return ResponseEntity.ok(assembleiaRepository.findAll());
    }


}
