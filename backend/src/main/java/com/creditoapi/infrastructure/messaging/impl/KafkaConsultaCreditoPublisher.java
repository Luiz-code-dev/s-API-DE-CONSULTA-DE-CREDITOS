package com.creditoapi.infrastructure.messaging.impl;

import com.creditoapi.application.dto.ConsultaEventDTO;
import com.creditoapi.infrastructure.messaging.ConsultaCreditoPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
public class KafkaConsultaCreditoPublisher implements ConsultaCreditoPublisher {

    private final KafkaTemplate<String, ConsultaEventDTO> kafkaTemplate;

    @Value("${app.kafka.topic.consulta-credito}")
    private String topicName;

    @Override
    public void publish(ConsultaEventDTO event) {
        log.debug("Publicando evento de consulta no Kafka: {}", event);

        CompletableFuture.runAsync(() -> 
            kafkaTemplate.send(topicName, event.tipoConsulta(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Erro ao publicar evento no Kafka: {}", ex.getMessage(), ex);
                    } else {
                        log.info("Evento publicado com sucesso no tópico {} - offset: {}",
                                topicName, result.getRecordMetadata().offset());
                    }
                })
        );
    }
}
