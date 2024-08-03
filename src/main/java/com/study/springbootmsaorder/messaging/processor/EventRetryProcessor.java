package com.study.springbootmsaorder.messaging.processor;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.study.springbootmsaorder.domain.outbox.domain.EventStatus;
import com.study.springbootmsaorder.domain.outbox.domain.Outbox;
import com.study.springbootmsaorder.domain.outbox.domain.OutboxRepository;
import com.study.springbootmsaorder.domain.outbox.service.OutboxService;
import com.study.springbootmsaorder.messaging.producer.KafkaProducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventRetryProcessor {

    private final OutboxService outboxService;
    private final OutboxRepository outboxRepository;
    private final KafkaProducer kafkaProducer;

    /***
     * Outbox Table에서 실패한 이벤트들을 불러와서 Retry 하는 메서드
     */
    @Transactional
    public void processFailedEventRetry() {
        log.info("[FailedEventService Start]");
        final List<Outbox> outboxList = outboxRepository.findByEventStatus(EventStatus.FAILED);

        for (final Outbox outbox : outboxList) {
            try {
                outbox.updateEventStatus(EventStatus.COMPLETED);

                kafkaProducer.send(outbox.getTopic().getValue(), outboxService.convertPayload(outbox.getPayload(),
                        outbox.getPayloadType()));
            } catch (final RuntimeException runtimeException) {
                outbox.updateEventStatus(EventStatus.FAILED);
                outbox.increaseRetryCount();
            }
        }
        log.info("[FailedEventService End]");
    }
}
