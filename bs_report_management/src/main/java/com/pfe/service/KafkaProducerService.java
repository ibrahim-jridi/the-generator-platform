package com.pfe.service;

import com.pfe.service.dto.ActivityAuditDTO;
import com.pfe.service.dto.ErrorAuditDTO;
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
    private final KafkaTemplate<String, ActivityAuditDTO> kafkaTemplateActivityAudit;

    private final KafkaTemplate<String, ErrorAuditDTO> kafkaTemplateErrorAudit;

    public KafkaProducerService(KafkaTemplate<String, ActivityAuditDTO> kafkaTemplateActivityAudit, KafkaTemplate<String, ErrorAuditDTO> kafkaTemplateErrorAudit) {
        this.kafkaTemplateActivityAudit = kafkaTemplateActivityAudit;
        this.kafkaTemplateErrorAudit = kafkaTemplateErrorAudit;
    }

    public void sendKafkaEventActivityAudit(ActivityAuditDTO event)
    {
        Message<ActivityAuditDTO> message = MessageBuilder
            .withPayload(event)
            .setHeader(KafkaHeaders.TOPIC, topicActivityAudit)
            .build();
        kafkaTemplateActivityAudit.send(message);
    }


    public void sendKafkaEventErrorAudit(ErrorAuditDTO event)
    {
        Message<ErrorAuditDTO> message = MessageBuilder
            .withPayload(event)
            .setHeader(KafkaHeaders.TOPIC, topicErrorAudit)
            .build();
        kafkaTemplateErrorAudit.send(message);
    }

}
