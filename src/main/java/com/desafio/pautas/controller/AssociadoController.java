package com.desafio.pautas.controller;

import com.desafio.pautas.entity.Associado;
import com.desafio.pautas.repository.AssociadoRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/associado")
public class AssociadoController {

    private AssociadoRepository associadoRepository;

    AssociadoController(AssociadoRepository associadoRepository) {
        this.associadoRepository = associadoRepository;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Registra novo associado")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Associado Saved Successfully"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<Associado> novoAssociado(@ApiParam(value = "Associado Request to add new.", required = true)
                                                   @Valid @RequestBody Associado associado) {
        return ResponseEntity.ok(this.associadoRepository.save(associado));
    }

    @GetMapping("/{associadoId}")
    @ApiOperation(value = "Find Associado by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Associado Returned Successfully"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<Associado> obterAssociadoPorId(@ApiParam(value = "Request to find Associado by ID", required = true)
                                                         @Valid @PathVariable Long associadoId) {
        return ResponseEntity.ok((this.associadoRepository.getById(associadoId)));
    }

    @GetMapping("/findAll")
    @ApiOperation(value = "Find All Associado")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Associados Returned Successfully"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<List<Associado>> findAll() {
        return ResponseEntity.ok(this.associadoRepository.findAll());
    }
}
