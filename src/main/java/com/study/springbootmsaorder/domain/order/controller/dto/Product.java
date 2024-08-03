package com.study.springbootmsaorder.domain.order.controller.dto;

import com.study.springbootmsaorder.domain.order.domain.OrderProduct;

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
