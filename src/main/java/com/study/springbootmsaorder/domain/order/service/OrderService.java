package com.study.springbootmsaorder.domain.order.service;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.springbootmsaorder.domain.order.controller.dto.OrderCancelRequest;
import com.study.springbootmsaorder.domain.order.controller.dto.OrderCreateRequest;
import com.study.springbootmsaorder.domain.order.domain.Order;
import com.study.springbootmsaorder.domain.order.domain.OrderProduct;
import com.study.springbootmsaorder.domain.order.domain.OrderStatus;
import com.study.springbootmsaorder.domain.order.domain.repository.OrderProductRepository;
import com.study.springbootmsaorder.domain.order.domain.repository.OrderRepository;
import com.study.springbootmsaorder.domain.order.service.dto.PaymentCancelEvent;
import com.study.springbootmsaorder.domain.outbox.domain.AggregateType;
import com.study.springbootmsaorder.domain.outbox.domain.EventType;
import com.study.springbootmsaorder.domain.outbox.domain.Outbox;
import com.study.springbootmsaorder.domain.outbox.service.OutboxService;
import com.study.springbootmsaorder.messaging.producer.Topic;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OutboxService<PaymentCancelEvent> outboxService;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 주문 생성
     * @param orderCreateRequest
     * @return orderId
     */
    @Transactional
    public Long createOrder(final OrderCreateRequest orderCreateRequest) {
        // TODO 상품 재고 감소 요청
        final Order order = orderCreateRequest.toOrderEntity();
        final Order savedOrder = orderRepository.save(order);

        final List<OrderProduct> orderProducts = orderCreateRequest.products()
                .stream()
                .map(product -> product.toOrderProductEntity(savedOrder.getId()))
                .toList();

        // Batch 처리 필요
        orderProductRepository.saveAll(orderProducts);

        return savedOrder.getId();
    }

    /***
     * 주문 취소
     * @param orderId
     * @param orderCancelRequest
     */
    @Transactional
    public void cancelOrder(final Long orderId, final OrderCancelRequest orderCancelRequest) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("order id not found"));

        order.updateOrderStatus(OrderStatus.CANCELED);

        // TODO 상품 재고 증가 요청

        final PaymentCancelEvent paymentCancelEvent = PaymentCancelEvent.builder()
                .orderId(orderId)
                .memberId(orderCancelRequest.memberId())
                .build();

        final Outbox outbox = outboxService.saveEventToOutbox(
                AggregateType.PAYMENT,
                EventType.PAYMENT_CANCEL,
                Topic.PAYMENT_CANCEL,
                paymentCancelEvent
        );

        paymentCancelEvent.updateOutboxId(outbox.getId());

        applicationEventPublisher.publishEvent(paymentCancelEvent);
    }
}
