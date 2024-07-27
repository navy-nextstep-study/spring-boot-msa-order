package com.study.springbootmsaorder.order.controller.dto;

import com.study.springbootmsaorder.order.domain.Order;

public record OrderCreateRequest(Long memberId, Long productId, Long quantity, Long totalPrice) {

    public Order toOrderEntity() {
        return Order.builder()
                .memberId(memberId)
                .productId(productId)
                .quantity(quantity)
                .totalPrice(totalPrice)
                .build();
    }
}
