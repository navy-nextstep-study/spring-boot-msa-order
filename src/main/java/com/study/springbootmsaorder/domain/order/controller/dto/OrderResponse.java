package com.study.springbootmsaorder.domain.order.controller.dto;

import java.util.List;

import com.study.springbootmsaorder.domain.order.domain.OrderStatus;

import lombok.Builder;

@Builder
public record OrderResponse(
        Long orderId,
        Long memberId,
        Long totalPrice,
        OrderStatus orderStatus,
        List<OrderProductResponse> orderProductResponses
) {

}
