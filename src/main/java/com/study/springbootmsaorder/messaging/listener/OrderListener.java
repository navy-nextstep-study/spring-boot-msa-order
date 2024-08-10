package com.study.springbootmsaorder.messaging.listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.study.springbootmsaorder.domain.order.service.dto.OrderCancelEvent;
import com.study.springbootmsaorder.domain.outbox.domain.EventStatus;
import com.study.springbootmsaorder.domain.outbox.service.OutboxService;
import com.study.springbootmsaorder.messaging.producer.KafkaProducer;
import com.study.springbootmsaorder.messaging.producer.Topic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderListener {

    private final KafkaProducer kafkaProducer;
    private final OutboxService outboxService;

    /***
     * 주문 취소 후 결제 취소 이벤트 발행
     * @param orderCancelEvent
     */
    @Async
    @TransactionalEventListener
    public void cancel(final OrderCancelEvent orderCancelEvent) {
        try {
            outboxService.updateOutboxStatus(orderCancelEvent.getOutboxId(), EventStatus.COMPLETED);
            kafkaProducer.send(Topic.ORDER_CANCEL.getValue(), orderCancelEvent);
        } catch (final RuntimeException runtimeException) {
            log.info("[{}] 메시지 전달 실패", Topic.ORDER_CANCEL.getValue(), runtimeException);
            outboxService.updateOutboxStatus(orderCancelEvent.getOutboxId(), EventStatus.FAILED);
        }
    }
}
