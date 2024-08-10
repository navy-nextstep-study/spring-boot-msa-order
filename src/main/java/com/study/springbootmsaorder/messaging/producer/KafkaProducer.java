package com.study.springbootmsaorder.messaging.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.study.springbootmsaorder.messaging.dto.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Message<?>> kafkaTemplate;

    public void send(final String topic, Object payload) {

        kafkaTemplate.send(topic, new Message<>(payload));
    }
}
