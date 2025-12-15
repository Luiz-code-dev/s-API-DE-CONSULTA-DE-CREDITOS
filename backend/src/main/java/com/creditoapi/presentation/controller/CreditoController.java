package com.creditoapi.presentation.controller;

import com.creditoapi.application.dto.CreditoDTO;
import com.creditoapi.application.service.CreditoService;
import com.creditoapi.presentation.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/creditos")
@RequiredArgsConstructor
@Tag(name = "Créditos", description = "API para consulta de créditos constituídos")
@CrossOrigin(origins = "*")
public class CreditoController {

    private final CreditoService creditoService;

    @GetMapping("/{numeroNfse}")
    @Operation(summary = "Buscar créditos por NFS-e", 
               description = "Retorna uma lista de créditos constituídos com base no número da NFS-e")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de créditos retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum crédito encontrado para a NFS-e informada")
    })
    public ResponseEntity<List<CreditoDTO>> findByNumeroNfse(
            @Parameter(description = "Número identificador da NFS-e", required = true)
            @PathVariable String numeroNfse) {

        log.info("Recebida requisição para buscar créditos por NFS-e: {}", numeroNfse);

        List<CreditoDTO> creditos = creditoService.findByNumeroNfse(numeroNfse);

        if (creditos.isEmpty()) {
            log.warn("Nenhum crédito encontrado para NFS-e: {}", numeroNfse);
            throw new ResourceNotFoundException("Nenhum crédito encontrado para a NFS-e: " + numeroNfse);
        }

        return ResponseEntity.ok(creditos);
    }

    @GetMapping("/credito/{numeroCredito}")
    @Operation(summary = "Buscar crédito por número", 
               description = "Retorna os detalhes de um crédito constituído específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Crédito retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Crédito não encontrado")
    })
    public ResponseEntity<CreditoDTO> findByNumeroCredito(
            @Parameter(description = "Número identificador do crédito constituído", required = true)
            @PathVariable String numeroCredito) {

        log.info("Recebida requisição para buscar crédito por número: {}", numeroCredito);

        return creditoService.findByNumeroCredito(numeroCredito)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> {
                    log.warn("Crédito não encontrado: {}", numeroCredito);
                    return new ResourceNotFoundException("Crédito não encontrado: " + numeroCredito);
                });
    }
}
