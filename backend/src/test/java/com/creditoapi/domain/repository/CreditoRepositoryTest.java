package com.creditoapi.domain.repository;

import com.creditoapi.domain.entity.Credito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("CreditoRepository Tests")
class CreditoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CreditoRepository creditoRepository;

    private Credito credito1;
    private Credito credito2;
    private Credito credito3;

    @BeforeEach
    void setUp() {
        credito1 = Credito.builder()
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

        credito2 = Credito.builder()
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

        credito3 = Credito.builder()
                .numeroCredito("654321")
                .numeroNfse("1122334")
                .dataConstituicao(LocalDate.of(2024, 1, 15))
                .valorIssqn(new BigDecimal("800.50"))
                .tipoCredito("Outros")
                .simplesNacional(true)
                .aliquota(new BigDecimal("3.5"))
                .valorFaturado(new BigDecimal("20000.00"))
                .valorDeducao(new BigDecimal("3000.00"))
                .baseCalculo(new BigDecimal("17000.00"))
                .build();

        entityManager.persist(credito1);
        entityManager.persist(credito2);
        entityManager.persist(credito3);
        entityManager.flush();
    }

    @Test
    @DisplayName("Deve retornar lista de créditos quando buscar por NFS-e existente")
    void findByNumeroNfse_WhenNfseExists_ShouldReturnCreditoList() {
        List<Credito> result = creditoRepository.findByNumeroNfse("7891011");

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Credito::getNumeroCredito)
                .containsExactlyInAnyOrder("123456", "789012");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando buscar por NFS-e inexistente")
    void findByNumeroNfse_WhenNfseNotExists_ShouldReturnEmptyList() {
        List<Credito> result = creditoRepository.findByNumeroNfse("999999");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar crédito quando buscar por número de crédito existente")
    void findByNumeroCredito_WhenCreditoExists_ShouldReturnCredito() {
        Optional<Credito> result = creditoRepository.findByNumeroCredito("123456");

        assertThat(result).isPresent();
        assertThat(result.get().getNumeroNfse()).isEqualTo("7891011");
        assertThat(result.get().getValorIssqn()).isEqualByComparingTo(new BigDecimal("1500.75"));
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando buscar por número de crédito inexistente")
    void findByNumeroCredito_WhenCreditoNotExists_ShouldReturnEmpty() {
        Optional<Credito> result = creditoRepository.findByNumeroCredito("999999");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar apenas um crédito para NFS-e com registro único")
    void findByNumeroNfse_WhenSingleRecord_ShouldReturnSingleCredito() {
        List<Credito> result = creditoRepository.findByNumeroNfse("1122334");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNumeroCredito()).isEqualTo("654321");
        assertThat(result.get(0).getTipoCredito()).isEqualTo("Outros");
    }
}
