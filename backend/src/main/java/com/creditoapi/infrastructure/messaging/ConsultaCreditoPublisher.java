package com.creditoapi.infrastructure.messaging;

import com.creditoapi.application.dto.ConsultaEventDTO;

public interface ConsultaCreditoPublisher {

    void publish(ConsultaEventDTO event);
}
