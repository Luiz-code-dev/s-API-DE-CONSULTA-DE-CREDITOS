package com.creditoapi.application.service;

import com.creditoapi.application.dto.CreditoDTO;
import com.creditoapi.application.mapper.CreditoMapper;
import com.creditoapi.application.service.impl.CreditoServiceImpl;
import com.creditoapi.domain.entity.Credito;
import com.creditoapi.domain.repository.CreditoRepository;
import com.creditoapi.infrastructure.messaging.ConsultaCreditoPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreditoService Tests")
class CreditoServiceTest {

    @Mock
    private CreditoRepository creditoRepository;

    @Mock
    private CreditoMapper creditoMapper;

    @Mock
    private ConsultaCreditoPublisher consultaPublisher;

    @InjectMocks
    private CreditoServiceImpl creditoService;

    private Credito credito;
    private CreditoDTO creditoDTO;

    @BeforeEach
    void setUp() {
        credito = Credito.builder()
                .id(1L)
                .numeroCredito("123456")
                .numeroNfse("7891011")
                .dataConstituicao(LocalDate.of(2024, 2, 25))
                .valorIssqn(new BigDecimal("1500.75"))
                .tipoCredito("ISSQN")
                .simplesNacional(true)
                .aliquota(new BigDecimal("5.0"))
                .valorFaturado(new BigDecimal("30000.00"))
                .valorDeducao(new BigDecimal("5000.00"))
                .baseCalculo(new BigDecimal("25000.00"))
                .build();

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
    }

    @Test
    @DisplayName("Deve retornar lista de créditos quando buscar por NFS-e existente")
    void findByNumeroNfse_WhenNfseExists_ShouldReturnCreditoList() {
        String numeroNfse = "7891011";
        List<Credito> creditos = List.of(credito);
        List<CreditoDTO> creditoDTOs = List.of(creditoDTO);

        when(creditoRepository.findByNumeroNfse(numeroNfse)).thenReturn(creditos);
        when(creditoMapper.toDTOList(creditos)).thenReturn(creditoDTOs);
        doNothing().when(consultaPublisher).publish(any());

        List<CreditoDTO> result = creditoService.findByNumeroNfse(numeroNfse);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().numeroCredito()).isEqualTo("123456");
        assertThat(result.getFirst().numeroNfse()).isEqualTo("7891011");

        verify(creditoRepository, times(1)).findByNumeroNfse(numeroNfse);
        verify(creditoMapper, times(1)).toDTOList(creditos);
        verify(consultaPublisher, times(1)).publish(any());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando NFS-e não existe")
    void findByNumeroNfse_WhenNfseNotExists_ShouldReturnEmptyList() {
        String numeroNfse = "999999";

        when(creditoRepository.findByNumeroNfse(numeroNfse)).thenReturn(Collections.emptyList());
        when(creditoMapper.toDTOList(Collections.emptyList())).thenReturn(Collections.emptyList());
        doNothing().when(consultaPublisher).publish(any());

        List<CreditoDTO> result = creditoService.findByNumeroNfse(numeroNfse);

        assertThat(result).isEmpty();

        verify(creditoRepository, times(1)).findByNumeroNfse(numeroNfse);
        verify(consultaPublisher, times(1)).publish(any());
    }

    @Test
    @DisplayName("Deve retornar crédito quando buscar por número de crédito existente")
    void findByNumeroCredito_WhenCreditoExists_ShouldReturnCredito() {
        String numeroCredito = "123456";

        when(creditoRepository.findByNumeroCredito(numeroCredito)).thenReturn(Optional.of(credito));
        when(creditoMapper.toDTO(credito)).thenReturn(creditoDTO);
        doNothing().when(consultaPublisher).publish(any());

        Optional<CreditoDTO> result = creditoService.findByNumeroCredito(numeroCredito);

        assertThat(result).isPresent();
        assertThat(result.get().numeroCredito()).isEqualTo("123456");
        assertThat(result.get().valorIssqn()).isEqualTo(new BigDecimal("1500.75"));
        assertThat(result.get().simplesNacional()).isEqualTo("Sim");

        verify(creditoRepository, times(1)).findByNumeroCredito(numeroCredito);
        verify(creditoMapper, times(1)).toDTO(credito);
        verify(consultaPublisher, times(1)).publish(any());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando crédito não existe")
    void findByNumeroCredito_WhenCreditoNotExists_ShouldReturnEmpty() {
        String numeroCredito = "999999";

        when(creditoRepository.findByNumeroCredito(numeroCredito)).thenReturn(Optional.empty());
        doNothing().when(consultaPublisher).publish(any());

        Optional<CreditoDTO> result = creditoService.findByNumeroCredito(numeroCredito);

        assertThat(result).isEmpty();

        verify(creditoRepository, times(1)).findByNumeroCredito(numeroCredito);
        verify(creditoMapper, never()).toDTO(any());
        verify(consultaPublisher, times(1)).publish(any());
    }
}
