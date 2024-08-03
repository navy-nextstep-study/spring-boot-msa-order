package com.study.springbootmsaorder.domain.order.controller.dto;

import lombok.Builder;

@Builder
public record OrderProductResponse(Long orderProductId, Long orderId, Long productId, Long quantity, Long unitPrice) {

}
