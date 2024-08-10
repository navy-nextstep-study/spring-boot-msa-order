package com.study.springbootmsaorder.domain.order.service.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderCancelEvent {

    private Long orderId;
    private Long memberId;
    private List<ProductStock> productStocks;
    private Long outboxId;

    @Builder
    public OrderCancelEvent(final Long orderId, final Long memberId, final List<ProductStock> productStocks) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.productStocks = productStocks;
    }

    public void updateOutboxId(final Long outboxId) {
        this.outboxId = outboxId;
    }
}
