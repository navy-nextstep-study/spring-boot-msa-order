package com.study.springbootmsaorder.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.springbootmsaorder.order.controller.dto.OrderCreateRequest;
import com.study.springbootmsaorder.order.domain.Order;
import com.study.springbootmsaorder.order.domain.OrderRepository;
import com.study.springbootmsaorder.order.domain.OrderStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    /**
     * 주문 생성
     * @param orderCreateRequest
     * @return orderId
     */
    @Transactional
    public Long createOrder(final OrderCreateRequest orderCreateRequest) {
        final Order order = orderCreateRequest.toOrderEntity();

        // 상품 재고 감소 요청

        // 상품 결제 요청

        order.updateOrderStatus(OrderStatus.COMPLETED);
        final Order savedOrder = orderRepository.save(order);

        return savedOrder.getId();
    }
}
