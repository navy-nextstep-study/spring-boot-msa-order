package com.study.springbootmsaorder.domain.order.service.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductStockIncreaseEvent {

    private List<ProductStock> productStocks;
    private Long outboxId;

    public ProductStockIncreaseEvent(final List<ProductStock> productStocks) {
        this.productStocks = productStocks;
    }

    public void updateOutboxId(final Long outboxId) {
        this.outboxId = outboxId;
    }
}
