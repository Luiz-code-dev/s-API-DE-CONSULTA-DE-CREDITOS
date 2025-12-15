package com.creditoapi.application.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ConsultaEventDTO(
        String tipoConsulta,
        String parametroConsulta,
        LocalDateTime dataHoraConsulta,
        int quantidadeResultados,
        boolean sucesso
) {
}
