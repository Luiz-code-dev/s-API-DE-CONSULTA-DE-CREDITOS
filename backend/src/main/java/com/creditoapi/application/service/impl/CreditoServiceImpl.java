package com.creditoapi.application.service.impl;

import com.creditoapi.application.dto.ConsultaEventDTO;
import com.creditoapi.application.dto.CreditoDTO;
import com.creditoapi.application.mapper.CreditoMapper;
import com.creditoapi.application.service.CreditoService;
import com.creditoapi.domain.entity.Credito;
import com.creditoapi.domain.repository.CreditoRepository;
import com.creditoapi.infrastructure.messaging.ConsultaCreditoPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreditoServiceImpl implements CreditoService {

    private final CreditoRepository creditoRepository;
    private final CreditoMapper creditoMapper;
    private final ConsultaCreditoPublisher consultaPublisher;

    @Override
    public List<CreditoDTO> findByNumeroNfse(String numeroNfse) {
        log.debug("Buscando créditos pelo número NFS-e: {}", numeroNfse);

        List<Credito> creditos = creditoRepository.findByNumeroNfse(numeroNfse);
        List<CreditoDTO> result = creditoMapper.toDTOList(creditos);

        publishConsultaEvent("NFSE", numeroNfse, result.size(), true);

        log.info("Encontrados {} créditos para NFS-e: {}", result.size(), numeroNfse);
        return result;
    }

    @Override
    public Optional<CreditoDTO> findByNumeroCredito(String numeroCredito) {
        log.debug("Buscando crédito pelo número: {}", numeroCredito);

        Optional<CreditoDTO> result = creditoRepository.findByNumeroCredito(numeroCredito)
                .map(creditoMapper::toDTO);

        publishConsultaEvent("CREDITO", numeroCredito, result.isPresent() ? 1 : 0, true);

        log.info("Crédito {} para número: {}", result.isPresent() ? "encontrado" : "não encontrado", numeroCredito);
        return result;
    }

    private void publishConsultaEvent(String tipoConsulta, String parametro, int quantidade, boolean sucesso) {
        ConsultaEventDTO event = ConsultaEventDTO.builder()
                .tipoConsulta(tipoConsulta)
                .parametroConsulta(parametro)
                .dataHoraConsulta(LocalDateTime.now())
                .quantidadeResultados(quantidade)
                .sucesso(sucesso)
                .build();

        consultaPublisher.publish(event);
    }
}
