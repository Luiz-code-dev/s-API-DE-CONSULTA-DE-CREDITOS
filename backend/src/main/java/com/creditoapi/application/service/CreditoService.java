package com.creditoapi.application.service;

import com.creditoapi.application.dto.CreditoDTO;

import java.util.List;
import java.util.Optional;

public interface CreditoService {

    List<CreditoDTO> findByNumeroNfse(String numeroNfse);

    Optional<CreditoDTO> findByNumeroCredito(String numeroCredito);
}
