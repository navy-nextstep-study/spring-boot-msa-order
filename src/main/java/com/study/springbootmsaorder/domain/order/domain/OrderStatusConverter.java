package com.study.springbootmsaorder.domain.order.domain;

import jakarta.persistence.AttributeConverter;

public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {

    @Override
    public String convertToDatabaseColumn(final OrderStatus orderStatus) {
        return orderStatus.getValue();
    }

    @Override
    public OrderStatus convertToEntityAttribute(final String orderStatusString) {
        return OrderStatus.of(orderStatusString);
    }
}
