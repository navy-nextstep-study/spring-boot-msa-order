package com.study.springbootmsaorder.domain.order.service.dto;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductQuantityUpdateRequest {

    private final List<ProductStock> productStocks;

}
