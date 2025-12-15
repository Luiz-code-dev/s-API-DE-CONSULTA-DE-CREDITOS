package com.creditoapi.presentation.controller;

import com.creditoapi.application.dto.CreditoDTO;
import com.creditoapi.application.service.CreditoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CreditoController.class)
@DisplayName("CreditoController Tests")
class CreditoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreditoService creditoService;

    private CreditoDTO creditoDTO;
    private CreditoDTO creditoDTO2;

    @BeforeEach
    void setUp() {
        creditoDTO = CreditoDTO.builder()
                .numeroCredito("123456")
                .numeroNfse("7891011")
                .dataConstituicao(LocalDate.of(2024, 2, 25))
                .valorIssqn(new BigDecimal("1500.75"))
                .tipoCredito("ISSQN")
                .simplesNacional("Sim")
                .aliquota(new BigDecimal("5.0"))
                .valorFaturado(new BigDecimal("30000.00"))
                .valorDeducao(new BigDecimal("5000.00"))
                .baseCalculo(new BigDecimal("25000.00"))
                .build();

        creditoDTO2 = CreditoDTO.builder()
                .numeroCredito("789012")
                .numeroNfse("7891011")
                .dataConstituicao(LocalDate.of(2024, 2, 26))
                .valorIssqn(new BigDecimal("1200.50"))
                .tipoCredito("ISSQN")
                .simplesNacional("Não")
                .aliquota(new BigDecimal("4.5"))
                .valorFaturado(new BigDecimal("25000.00"))
                .valorDeducao(new BigDecimal("4000.00"))
                .baseCalculo(new BigDecimal("21000.00"))
                .build();
    }

    @Test
    @DisplayName("GET /api/creditos/{numeroNfse} - Deve retornar lista de créditos")
    void findByNumeroNfse_WhenNfseExists_ShouldReturnCreditoList() throws Exception {
        String numeroNfse = "7891011";
        when(creditoService.findByNumeroNfse(numeroNfse))
                .thenReturn(List.of(creditoDTO, creditoDTO2));

        mockMvc.perform(get("/api/creditos/{numeroNfse}", numeroNfse)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].numeroCredito", is("123456")))
                .andExpect(jsonPath("$[0].numeroNfse", is("7891011")))
                .andExpect(jsonPath("$[0].valorIssqn", is(1500.75)))
                .andExpect(jsonPath("$[0].tipoCredito", is("ISSQN")))
                .andExpect(jsonPath("$[0].simplesNacional", is("Sim")))
                .andExpect(jsonPath("$[1].numeroCredito", is("789012")))
                .andExpect(jsonPath("$[1].simplesNacional", is("Não")));
    }

    @Test
    @DisplayName("GET /api/creditos/{numeroNfse} - Deve retornar 404 quando NFS-e não existe")
    void findByNumeroNfse_WhenNfseNotExists_ShouldReturn404() throws Exception {
        String numeroNfse = "999999";
        when(creditoService.findByNumeroNfse(numeroNfse))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/creditos/{numeroNfse}", numeroNfse)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Nenhum crédito encontrado")));
    }

    @Test
    @DisplayName("GET /api/creditos/credito/{numeroCredito} - Deve retornar crédito específico")
    void findByNumeroCredito_WhenCreditoExists_ShouldReturnCredito() throws Exception {
        String numeroCredito = "123456";
        when(creditoService.findByNumeroCredito(numeroCredito))
                .thenReturn(Optional.of(creditoDTO));

        mockMvc.perform(get("/api/creditos/credito/{numeroCredito}", numeroCredito)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.numeroCredito", is("123456")))
                .andExpect(jsonPath("$.numeroNfse", is("7891011")))
                .andExpect(jsonPath("$.dataConstituicao", is("2024-02-25")))
                .andExpect(jsonPath("$.valorIssqn", is(1500.75)))
                .andExpect(jsonPath("$.tipoCredito", is("ISSQN")))
                .andExpect(jsonPath("$.simplesNacional", is("Sim")))
                .andExpect(jsonPath("$.aliquota", is(5.0)))
                .andExpect(jsonPath("$.valorFaturado", is(30000.00)))
                .andExpect(jsonPath("$.valorDeducao", is(5000.00)))
                .andExpect(jsonPath("$.baseCalculo", is(25000.00)));
    }

    @Test
    @DisplayName("GET /api/creditos/credito/{numeroCredito} - Deve retornar 404 quando crédito não existe")
    void findByNumeroCredito_WhenCreditoNotExists_ShouldReturn404() throws Exception {
        String numeroCredito = "999999";
        when(creditoService.findByNumeroCredito(numeroCredito))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/creditos/credito/{numeroCredito}", numeroCredito)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Crédito não encontrado")));
    }
}
