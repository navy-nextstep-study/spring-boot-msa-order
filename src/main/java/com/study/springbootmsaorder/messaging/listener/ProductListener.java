package com.study.springbootmsaorder.messaging.listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.study.springbootmsaorder.domain.order.service.dto.ProductStockIncreaseEvent;
import com.study.springbootmsaorder.domain.outbox.domain.EventStatus;
import com.study.springbootmsaorder.domain.outbox.service.OutboxService;
import com.study.springbootmsaorder.messaging.producer.KafkaProducer;
import com.study.springbootmsaorder.messaging.producer.Topic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductListener {

    private final KafkaProducer kafkaProducer;
    private final OutboxService outboxService;

    @Async
    @TransactionalEventListener
    public void productStockIncrease(final ProductStockIncreaseEvent productStockIncreaseEvent) {
        try {
            outboxService.updateOutboxStatus(productStockIncreaseEvent.getOutboxId(), EventStatus.COMPLETED);
            kafkaProducer.send(Topic.PRODUCT_STOCK_INCREASE.getValue(), productStockIncreaseEvent);
        } catch (final RuntimeException runtimeException) {
            log.info("[{}] 메시지 전달 실패", Topic.PRODUCT_STOCK_INCREASE.getValue(), runtimeException);
            outboxService.updateOutboxStatus(productStockIncreaseEvent.getOutboxId(), EventStatus.FAILED);
        }
    }
}
