package com.pfe.services;


import com.pfe.dto.ErrorAuditDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Value("${spring.kafka.topic.activity-audit.name}")
    private String topicActivityAudit;

    @Value("${spring.kafka.topic.error-audit.name}")
    private String topicErrorAudit;

    @Value("${spring.kafka.enabled}")
    private boolean kafkaEnabled;

    private KafkaTemplate<String, ErrorAuditDTO> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, ErrorAuditDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendKafkaEventErrorAudit(ErrorAuditDTO event) {
        if (kafkaEnabled) {
            Message<ErrorAuditDTO> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, topicErrorAudit)
                .build();
            kafkaTemplate.send(message);
        }

    }

}
