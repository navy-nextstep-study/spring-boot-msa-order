package com.study.springbootmsaorder.global.api;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PatchExchange;

import com.study.springbootmsaorder.domain.order.controller.dto.Product;

@Component
@HttpExchange
public interface ProductHttpClient {

    @PatchExchange("/products/stock")
    void decreaseProductStock(@RequestBody final List<Product> products);
}
