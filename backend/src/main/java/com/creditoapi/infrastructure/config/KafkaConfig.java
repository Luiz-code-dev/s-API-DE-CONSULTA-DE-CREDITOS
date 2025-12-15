package com.creditoapi.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
public class KafkaConfig {

    @Value("${app.kafka.topic.consulta-credito}")
    private String consultaCreditoTopic;

    @Bean
    public NewTopic consultaCreditoTopic() {
        return TopicBuilder.name(consultaCreditoTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
