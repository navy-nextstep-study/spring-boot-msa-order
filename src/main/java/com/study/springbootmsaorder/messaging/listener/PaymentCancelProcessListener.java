package com.study.springbootmsaorder.messaging.listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.study.springbootmsaorder.domain.order.service.dto.PaymentCancelEvent;
import com.study.springbootmsaorder.domain.outbox.domain.EventStatus;
import com.study.springbootmsaorder.domain.outbox.service.OutboxService;
import com.study.springbootmsaorder.messaging.producer.KafkaProducer;
import com.study.springbootmsaorder.messaging.producer.Topic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentCancelProcessListener {

    private final KafkaProducer kafkaProducer;
    private final OutboxService<String> outboxService;

    /***
     * 주문 취소 후 결제 취소 이벤트 발행
     * @param paymentCancelEvent
     */
    @Async
    @TransactionalEventListener
    public void process(final PaymentCancelEvent paymentCancelEvent) {
        try {
            outboxService.updateOutboxStatus(paymentCancelEvent.getOutboxId(), EventStatus.COMPLETED);
            kafkaProducer.send(Topic.PAYMENT_CANCEL.getValue(), paymentCancelEvent);
        } catch (final RuntimeException runtimeException) {
            log.info("[{}] 메시지 전달 실패", Topic.PAYMENT_CANCEL.getValue(), runtimeException);
            outboxService.updateOutboxStatus(paymentCancelEvent.getOutboxId(), EventStatus.FAILED);
        }
    }
}
