package com.desafio.pautas.controller;

import com.desafio.pautas.entity.Pauta;
import com.desafio.pautas.repository.PautaRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pautas")
public class PautaController {

    private PautaRepository pautaRepository;

    PautaController(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Registra nova pauta")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Pauta Added Successfully"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public Pauta addNewPauta(@ApiParam(value = "Pauta request para registrar nova pauta.", required = true)
                             @Valid @RequestBody Pauta pauta) {
        return pautaRepository.save(pauta);
    }

    @GetMapping("/findAll")
    @ApiOperation(value = "Find All Pautas")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Pautas Returned Successfully"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<List<Pauta>> findAll() {
        return ResponseEntity.ok(pautaRepository.findAll());
    }

    @GetMapping("/{pautaId}")
    @ApiOperation(value = "Find Pauta by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Pauta Returned Successfully"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public Pauta pautaById(@ApiParam(value = "Request Pauta by ID.", required = true)
                           @Valid @PathVariable Long pautaId) {
        return pautaRepository.findById(pautaId).orElseThrow(() -> new EntityNotFoundException("Pauta não encontrada."));
    }
}
