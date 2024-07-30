package com.study.springbootmsaorder.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.springbootmsaorder.order.controller.dto.OrderCreateRequest;
import com.study.springbootmsaorder.order.domain.Order;
import com.study.springbootmsaorder.order.domain.OrderProduct;
import com.study.springbootmsaorder.order.domain.repository.OrderProductRepository;
import com.study.springbootmsaorder.order.domain.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    /**
     * 주문 생성
     * @param orderCreateRequest
     * @return orderId
     */
    @Transactional
    public Long createOrder(final OrderCreateRequest orderCreateRequest) {
        // 상품 재고 감소 요청

        final Order order = orderCreateRequest.toOrderEntity();
        final Order savedOrder = orderRepository.save(order);

        final List<OrderProduct> orderProducts = orderCreateRequest.products()
                .stream()
                .map(product -> product.toOrderProductEntity(savedOrder.getId()))
                .toList();

        orderProductRepository.saveAll(orderProducts);

        return savedOrder.getId();
    }
}
