package com.study.springbootmsaorder.messaging.processor;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.study.springbootmsaorder.domain.outbox.domain.EventStatus;
import com.study.springbootmsaorder.domain.outbox.domain.Outbox;
import com.study.springbootmsaorder.domain.outbox.domain.OutboxRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventDeleteProcessor {

    private final OutboxRepository outboxRepository;

    /***
     * 완료된 이벤트를 삭제하는 로직
     * (Kafka로 전달된 이벤트가 계속 쌓이는 문제를 방지하기 위한 용도)
     */
    @Transactional
    public void processCompletedEventDelete() {
        log.info("[EventDeleteService Start]");
        final List<Outbox> outboxList = outboxRepository.findByEventStatus(EventStatus.COMPLETED);

        outboxRepository.deleteAll(outboxList);
        log.info("[EventDeleteService End]");
    }
}
