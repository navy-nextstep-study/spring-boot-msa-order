package com.study.springbootmsaorder.domain.order.service;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.springbootmsaorder.domain.order.controller.dto.OrderCancelRequest;
import com.study.springbootmsaorder.domain.order.controller.dto.OrderCreateRequest;
import com.study.springbootmsaorder.domain.order.controller.dto.OrderProductResponse;
import com.study.springbootmsaorder.domain.order.controller.dto.OrderResponse;
import com.study.springbootmsaorder.domain.order.controller.dto.Product;
import com.study.springbootmsaorder.domain.order.domain.Order;
import com.study.springbootmsaorder.domain.order.domain.OrderProduct;
import com.study.springbootmsaorder.domain.order.domain.OrderStatus;
import com.study.springbootmsaorder.domain.order.domain.repository.OrderProductRepository;
import com.study.springbootmsaorder.domain.order.domain.repository.OrderRepository;
import com.study.springbootmsaorder.domain.order.service.dto.OrderCancelEvent;
import com.study.springbootmsaorder.domain.order.service.dto.ProductQuantityUpdateRequest;
import com.study.springbootmsaorder.domain.order.service.dto.ProductStock;
import com.study.springbootmsaorder.domain.outbox.domain.AggregateType;
import com.study.springbootmsaorder.domain.outbox.domain.EventType;
import com.study.springbootmsaorder.domain.outbox.domain.Outbox;
import com.study.springbootmsaorder.domain.outbox.service.OutboxService;
import com.study.springbootmsaorder.global.api.ProductHttpClient;
import com.study.springbootmsaorder.messaging.producer.Topic;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OutboxService outboxService;
    private final OrderRepository orderRepository;
    private final ProductHttpClient productHttpClient;
    private final OrderProductRepository orderProductRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 주문 생성
     * @param orderCreateRequest
     * @return orderId
     */
    @Transactional
    public Long createOrder(final OrderCreateRequest orderCreateRequest) {
        final List<Product> products = orderCreateRequest.products();

        final List<ProductStock> productStocks = products.stream()
                .map(product -> new ProductStock(product.productId(), product.quantity() * -1))
                .toList();

        productHttpClient.decreaseProductStock(new ProductQuantityUpdateRequest(productStocks)); // 상품 재고 감소 요청

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
        final Order order = getOrderById(orderId);

        boolean isCanceled = isOrderStatusCanceled(order);

        if (isCanceled) {
            throw new IllegalArgumentException("이미 주문 취소된 건입니다.");
        }

        order.updateOrderStatus(OrderStatus.CANCELED);

        orderCancelEventPublish(order, orderCancelRequest.memberId()); // 주문 취소 이벤트 발행
    }

    private void orderCancelEventPublish(final Order order, final Long memberId) {
        final List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(order.getId());

        final List<ProductStock> productStocks = orderProducts.stream()
                .map(orderProduct -> ProductStock.builder()
                        .quantity(orderProduct.getQuantity())
                        .productId(orderProduct.getProductId())
                        .build()
                )
                .toList();

        final OrderCancelEvent orderCancelEvent = OrderCancelEvent.builder()
                .orderId(order.getId())
                .memberId(memberId)
                .productStocks(productStocks)
                .build();

        final Outbox outbox = outboxService.saveEventToOutbox(
                AggregateType.ORDER,
                EventType.ORDER_CANCEL,
                Topic.ORDER_CANCEL,
                orderCancelEvent
        );

        orderCancelEvent.updateOutboxId(outbox.getId());

        applicationEventPublisher.publishEvent(orderCancelEvent);
    }

    private Order getOrderById(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("order id not found"));
    }

    private boolean isOrderStatusCanceled(final Order order) {
        return order.getOrderStatus() == OrderStatus.CANCELED;
    }

    /***
     * 주문 조회
     * @param orderId
     * @return
     */
    public OrderResponse getOrder(final Long orderId) {
        final Order order = getOrderById(orderId);
        final List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(orderId);

        final List<OrderProductResponse> orderProductResponses = orderProducts.stream()
                .map(orderProduct -> OrderProductResponse.builder()
                        .orderProductId(orderProduct.getProductId())
                        .orderId(orderProduct.getOrderId())
                        .productId(orderProduct.getProductId())
                        .quantity(orderProduct.getQuantity())
                        .unitPrice(orderProduct.getUnitPrice())
                        .build()
                )
                .toList();

        return OrderResponse.builder()
                .orderId(order.getId())
                .memberId(order.getMemberId())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .orderProductResponses(orderProductResponses)
                .build();
    }
}
