package com.study.springbootmsaorder.domain.order.controller.dto;

import java.util.List;

import com.study.springbootmsaorder.domain.order.domain.Order;

public record OrderCreateRequest(Long memberId, Long totalPrice, List<Product> products) {

    public Order toOrderEntity() {
        return Order.builder()
                .memberId(memberId)
                .totalPrice(totalPrice)
                .build();
    }
}
