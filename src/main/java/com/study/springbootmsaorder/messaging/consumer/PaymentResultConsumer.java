package com.study.springbootmsaorder.messaging.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.study.springbootmsaorder.domain.order.domain.Order;
import com.study.springbootmsaorder.domain.order.domain.OrderStatus;
import com.study.springbootmsaorder.domain.order.domain.repository.OrderRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentResultConsumer {

    private final OrderRepository orderRepository;

    @Transactional
    @KafkaListener(topics = "payment-result")
    public void listenPaymentResult(@Payload final PaymentResult paymentResult) {
        log.info("Received payment result: {}", paymentResult);

        final OrderStatus orderStatus = OrderStatus.valueOf(paymentResult.status());

        switch (orderStatus) {
            case FAILED -> handlePaymentFailure(paymentResult);
            case COMPLETED -> handlePaymentSuccess(paymentResult);
            default -> throw new IllegalArgumentException("Invalid payment status: " + paymentResult.status());
        }
    }

    private void handlePaymentFailure(final PaymentResult paymentResult) {
        updateOrderStatus(paymentResult);
        // 재고 증가 요청
    }

    private void handlePaymentSuccess(final PaymentResult paymentResult) {
        updateOrderStatus(paymentResult);
    }

    private void updateOrderStatus(final PaymentResult paymentResult) {
        final Order order = findOrderById(paymentResult.orderId());
        order.updateOrderStatus(OrderStatus.valueOf(paymentResult.status()));
    }

    private Order findOrderById(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));
    }
}
