package com.study.springbootmsaorder.order.controller.dto;

import java.util.List;

import com.study.springbootmsaorder.order.domain.Order;
import com.study.springbootmsaorder.order.domain.OrderProduct;

public record OrderCreateRequest(Long memberId, Long totalPrice, List<Product> products) {

    public record Product(Long productId, Long quantity, Long unitPrice) {

        public OrderProduct toOrderProductEntity(final Long orderId) {
            return OrderProduct.builder()
                    .orderId(orderId)
                    .productId(productId)
                    .quantity(quantity)
                    .unitPrice(unitPrice)
                    .build();
        }
    }

    public Order toOrderEntity() {
        return Order.builder()
                .memberId(memberId)
                .totalPrice(totalPrice)
                .build();
    }
}
