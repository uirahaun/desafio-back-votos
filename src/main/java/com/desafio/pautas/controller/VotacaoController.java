package com.desafio.pautas.controller;

import com.desafio.pautas.dto.AssembleiaDTO;
import com.desafio.pautas.dto.VotacaoDTO;
import com.desafio.pautas.dto.VotacaoFinalDTO;
import com.desafio.pautas.entity.Assembleia;
import com.desafio.pautas.entity.Associado;
import com.desafio.pautas.entity.User;
import com.desafio.pautas.entity.Votacao;
import com.desafio.pautas.repository.AssembleiaRepository;
import com.desafio.pautas.repository.AssociadoRepository;
import com.desafio.pautas.repository.VotacaoRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/votacao")
public class VotacaoController {

    public static final String UNABLE_TO_VOTE = "UNABLE_TO_VOTE";
    private VotacaoRepository votacaoRepository;

    @Autowired
    private AssembleiaRepository assembleiaRepository;
    @Autowired
    private AssociadoRepository associadoRepository;

    VotacaoController(VotacaoRepository votacaoRepository) {
        this.votacaoRepository = votacaoRepository;
    }

    @ApiOperation(value = "Registra voto de associado")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Voted Successfully"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Votacao> computarVoto(@ApiParam(value = "Votação request para registrar voto de associado.", required = true)
                                                @Valid @RequestBody VotacaoDTO votacao) {
        Assembleia assembleia = assembleiaRepository.findById(votacao.getAssembleiaId()).orElseThrow(
                () -> {
                    throw new EntityNotFoundException("Assembléia não encontrada.");
                }
        );

        Associado associado = associadoRepository.findById(votacao.getAssociadoId()).orElseThrow(
                () -> {
                    throw new EntityNotFoundException("Associado não encontrado.");
                }
        );

        if (assembleia != null && LocalDateTime.now().isAfter(assembleia.getDataHoraFim())) {
            throw new RuntimeException("Votação encerrada.");
        }

        if (assembleia != null && LocalDateTime.now().isBefore(assembleia.getDataHoraInicio())) {
            throw new RuntimeException("Votação não iniciada.");
        }

        if (votacao.getVoto() == null || "".equalsIgnoreCase(votacao.getVoto())) {
            throw new EntityNotFoundException("Não é permitido voto em branco.");
        }

        if (votacao.getVoto() != null && (!"SIM".equalsIgnoreCase(votacao.getVoto()) &&
                !"NÃO".equalsIgnoreCase(votacao.getVoto()))) {
            throw new EntityNotFoundException("Apenas SIM ou NÃO é permitido como voto.");
        }

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User> user = restTemplate.getForEntity(
                "https://user-info.herokuapp.com/users/" + associado.getCpf(), User.class);

        if (user != null && UNABLE_TO_VOTE.equalsIgnoreCase(user.getBody().getStatus())) {
            throw new EntityNotFoundException("Associado não tem permissão de voto.");
        }

        Votacao newVotacao = new Votacao();
        newVotacao.setVoto(votacao.getVoto().toUpperCase(Locale.ROOT));
        newVotacao.setAssembleiaId(assembleia.getId());
        newVotacao.setAssociadoId(associado.getId());
        newVotacao.setDataHoraVotacao(LocalDateTime.now());

        return ResponseEntity.ok(this.votacaoRepository.save(newVotacao));
    }

    @GetMapping("/computeVotacao/{assembleiaId}")
    @ApiOperation(value = "Obter resultado da votação.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Iniciada com sucesso"),
            @ApiResponse(code = 401, message = "Você não tem autorização de acesso."),
            @ApiResponse(code = 404, message = "Erro no processamento.")
    })
    public VotacaoFinalDTO computeVotacao(@ApiParam(value = "Request Resultado da Votação")
                                          @Valid @PathVariable Long assembleiaId) {
        Assembleia assembleia = assembleiaRepository.findById(assembleiaId).orElseThrow(
                () -> {
                    throw new EntityNotFoundException("Assembléia não encontrada.");
                }
        );

        VotacaoFinalDTO votacaoFinalDTO = new VotacaoFinalDTO();
        votacaoFinalDTO.setAssembleia(new AssembleiaDTO());
        votacaoFinalDTO.getAssembleia().setDataHoraInicio(assembleia.getDataHoraInicio());
        votacaoFinalDTO.setTotalVotos(Long.MIN_VALUE);

        Votacao votacao = new Votacao();
        votacao.setAssembleiaId(assembleiaId);
        votacao.setVoto("SIM");
        Example<Votacao> votacaoExample = Example.of(votacao);

        List<Votacao> votosSIM = votacaoRepository.findAll(votacaoExample);

        votacao = new Votacao();
        votacao.setAssembleiaId(assembleiaId);
        votacao.setVoto("NÃO");
        votacaoExample = Example.of(votacao);
        List<Votacao> votosNAO = votacaoRepository.findAll(votacaoExample);

        votacaoFinalDTO.setTotalSim(Long.valueOf(votosSIM.size()));
        votacaoFinalDTO.setTotalNao(Long.valueOf(votosNAO.size()));

        votacaoFinalDTO.setTotalVotos(Long.sum(votacaoFinalDTO.getTotalNao(), votacaoFinalDTO.getTotalSim()));

        return votacaoFinalDTO;
    }
}
