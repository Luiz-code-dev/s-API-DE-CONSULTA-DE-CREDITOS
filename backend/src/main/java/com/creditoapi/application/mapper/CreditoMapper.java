package com.creditoapi.application.mapper;

import com.creditoapi.application.dto.CreditoDTO;
import com.creditoapi.domain.entity.Credito;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class CreditoMapper {

    private static final String SIM = "Sim";
    private static final String NAO = "Não";

    public CreditoDTO toDTO(Credito credito) {
        if (Objects.isNull(credito)) {
            return null;
        }

        return CreditoDTO.builder()
                .numeroCredito(credito.getNumeroCredito())
                .numeroNfse(credito.getNumeroNfse())
                .dataConstituicao(credito.getDataConstituicao())
                .valorIssqn(credito.getValorIssqn())
                .tipoCredito(credito.getTipoCredito())
                .simplesNacional(credito.isSimplesNacional() ? SIM : NAO)
                .aliquota(credito.getAliquota())
                .valorFaturado(credito.getValorFaturado())
                .valorDeducao(credito.getValorDeducao())
                .baseCalculo(credito.getBaseCalculo())
                .build();
    }

    public List<CreditoDTO> toDTOList(List<Credito> creditos) {
        if (Objects.isNull(creditos) || creditos.isEmpty()) {
            return Collections.emptyList();
        }
        
        return creditos.stream()
                .map(this::toDTO)
                .toList();
    }
}
