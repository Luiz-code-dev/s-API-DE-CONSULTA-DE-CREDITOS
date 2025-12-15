package com.creditoapi.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record CreditoDTO(
        String numeroCredito,
        String numeroNfse,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dataConstituicao,
        BigDecimal valorIssqn,
        String tipoCredito,
        String simplesNacional,
        BigDecimal aliquota,
        BigDecimal valorFaturado,
        BigDecimal valorDeducao,
        BigDecimal baseCalculo
) {
}
