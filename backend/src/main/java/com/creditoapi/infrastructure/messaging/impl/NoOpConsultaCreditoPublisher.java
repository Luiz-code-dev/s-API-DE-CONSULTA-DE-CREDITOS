package com.creditoapi.infrastructure.messaging.impl;

import com.creditoapi.application.dto.ConsultaEventDTO;
import com.creditoapi.infrastructure.messaging.ConsultaCreditoPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "false", matchIfMissing = true)
public class NoOpConsultaCreditoPublisher implements ConsultaCreditoPublisher {

    @Override
    public void publish(ConsultaEventDTO event) {
        log.debug("Kafka desabilitado - evento não publicado: {}", event);
    }
}
