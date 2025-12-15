package com.creditoapi.application.mapper;

import com.creditoapi.application.dto.CreditoDTO;
import com.creditoapi.domain.entity.Credito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CreditoMapper Tests")
class CreditoMapperTest {

    private CreditoMapper creditoMapper;

    @BeforeEach
    void setUp() {
        creditoMapper = new CreditoMapper();
    }

    @Test
    @DisplayName("Deve converter Credito para CreditoDTO com Simples Nacional = Sim")
    void toDTO_WithSimplesNacionalTrue_ShouldReturnDTOWithSim() {
        Credito credito = Credito.builder()
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

        CreditoDTO result = creditoMapper.toDTO(credito);

        assertThat(result).isNotNull();
        assertThat(result.numeroCredito()).isEqualTo("123456");
        assertThat(result.numeroNfse()).isEqualTo("7891011");
        assertThat(result.dataConstituicao()).isEqualTo(LocalDate.of(2024, 2, 25));
        assertThat(result.valorIssqn()).isEqualByComparingTo(new BigDecimal("1500.75"));
        assertThat(result.tipoCredito()).isEqualTo("ISSQN");
        assertThat(result.simplesNacional()).isEqualTo("Sim");
        assertThat(result.aliquota()).isEqualByComparingTo(new BigDecimal("5.0"));
        assertThat(result.valorFaturado()).isEqualByComparingTo(new BigDecimal("30000.00"));
        assertThat(result.valorDeducao()).isEqualByComparingTo(new BigDecimal("5000.00"));
        assertThat(result.baseCalculo()).isEqualByComparingTo(new BigDecimal("25000.00"));
    }

    @Test
    @DisplayName("Deve converter Credito para CreditoDTO com Simples Nacional = Não")
    void toDTO_WithSimplesNacionalFalse_ShouldReturnDTOWithNao() {
        Credito credito = Credito.builder()
                .id(2L)
                .numeroCredito("789012")
                .numeroNfse("7891011")
                .dataConstituicao(LocalDate.of(2024, 2, 26))
                .valorIssqn(new BigDecimal("1200.50"))
                .tipoCredito("ISSQN")
                .simplesNacional(false)
                .aliquota(new BigDecimal("4.5"))
                .valorFaturado(new BigDecimal("25000.00"))
                .valorDeducao(new BigDecimal("4000.00"))
                .baseCalculo(new BigDecimal("21000.00"))
                .build();

        CreditoDTO result = creditoMapper.toDTO(credito);

        assertThat(result).isNotNull();
        assertThat(result.simplesNacional()).isEqualTo("Não");
    }

    @Test
    @DisplayName("Deve retornar null quando Credito for null")
    void toDTO_WhenCreditoIsNull_ShouldReturnNull() {
        CreditoDTO result = creditoMapper.toDTO(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Deve converter lista de Credito para lista de CreditoDTO")
    void toDTOList_ShouldConvertListCorrectly() {
        Credito credito1 = Credito.builder()
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

        Credito credito2 = Credito.builder()
                .id(2L)
                .numeroCredito("789012")
                .numeroNfse("7891011")
                .dataConstituicao(LocalDate.of(2024, 2, 26))
                .valorIssqn(new BigDecimal("1200.50"))
                .tipoCredito("ISSQN")
                .simplesNacional(false)
                .aliquota(new BigDecimal("4.5"))
                .valorFaturado(new BigDecimal("25000.00"))
                .valorDeducao(new BigDecimal("4000.00"))
                .baseCalculo(new BigDecimal("21000.00"))
                .build();

        List<Credito> creditos = List.of(credito1, credito2);

        List<CreditoDTO> result = creditoMapper.toDTOList(creditos);

        assertThat(result).hasSize(2);
        assertThat(result.getFirst().numeroCredito()).isEqualTo("123456");
        assertThat(result.getFirst().simplesNacional()).isEqualTo("Sim");
        assertThat(result.get(1).numeroCredito()).isEqualTo("789012");
        assertThat(result.get(1).simplesNacional()).isEqualTo("Não");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando lista de Credito for vazia")
    void toDTOList_WhenListIsEmpty_ShouldReturnEmptyList() {
        List<CreditoDTO> result = creditoMapper.toDTOList(Collections.emptyList());

        assertThat(result).isEmpty();
    }
}
