package com.study.springbootmsaorder.domain.outbox.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.springbootmsaorder.domain.outbox.domain.AggregateType;
import com.study.springbootmsaorder.domain.outbox.domain.EventStatus;
import com.study.springbootmsaorder.domain.outbox.domain.EventType;
import com.study.springbootmsaorder.domain.outbox.domain.Outbox;
import com.study.springbootmsaorder.domain.outbox.domain.OutboxRepository;
import com.study.springbootmsaorder.messaging.producer.Topic;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService<T> {

    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;

    /***
     * Outbox Table에 이벤트 전송 기록 저장
     * @param aggregateType
     * @param eventType
     * @param topic
     * @param payload
     * @return
     */
    @Transactional
    public Outbox saveEventToOutbox(final AggregateType aggregateType, final EventType eventType, final Topic topic,
            final T payload) {
        Outbox savedOutbox = null;

        try {
            final Outbox outbox = Outbox.builder()
                    .aggregateType(aggregateType)
                    .eventType(eventType)
                    .topic(topic)
                    .payload(objectMapper.writeValueAsString(payload))
                    .payloadType(payload.getClass().getName())
                    .build();

            savedOutbox = outboxRepository.save(outbox);
        } catch (JsonProcessingException e) {
            log.error("Outbox Json 변환 실패");
        }

        return savedOutbox;
    }

    /***
     * 저장된 이벤트의 처리 결과 상태를 변경하는 메서드
     * @param outboxId
     * @param status
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateOutboxStatus(final Long outboxId, final EventStatus status) {
        final Outbox outbox = outboxRepository.findById(outboxId)
                .orElseThrow(() -> new EntityNotFoundException("Not Found OutboxId"));

        outbox.updateEventStatus(status);
    }

    /***
     * Outbox Table에 저장되는 Payload를 해당하는 Type의 Object로 만들기 위한 메서드
     * (실패한 메시지는 재전송이 되어야하는데, 이때 재전송되어야하는 메세지들은 전부 다른 형태의 Type을 가지고 있으므로 해당하는 Type으로 만들어주기 위함)
     * @param payload
     * @param payloadType
     * @return Object
     */
    public Object convertPayload(final String payload, final String payloadType) {
        try {
            Class<?> clazz = Class.forName(payloadType);
            return objectMapper.readValue(payload, clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found: " + payloadType, e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process JSON for class: " + payloadType, e);
        }
    }
}
