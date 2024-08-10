package com.study.springbootmsaorder.global.api;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PatchExchange;

import com.study.springbootmsaorder.domain.order.service.dto.ProductQuantityUpdateRequest;

@Component
@HttpExchange
public interface ProductHttpClient {

    @PatchExchange("/products/stock")
    void decreaseProductStock(@RequestBody final ProductQuantityUpdateRequest productQuantityUpdateRequest);
}
