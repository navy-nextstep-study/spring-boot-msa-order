package com.study.springbootmsaorder.domain.order.service.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductQuantityUpdateRequest {

    private List<ProductStock> productStocks;

    public ProductQuantityUpdateRequest(final List<ProductStock> productStocks) {
        this.productStocks = productStocks;
    }
}
