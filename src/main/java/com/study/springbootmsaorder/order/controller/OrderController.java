package com.study.springbootmsaorder.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.springbootmsaorder.order.controller.dto.OrderCreateRequest;
import com.study.springbootmsaorder.order.controller.dto.OrderCreateResponse;
import com.study.springbootmsaorder.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderCreateResponse> createOrder(@RequestBody final OrderCreateRequest orderCreateRequest) {
        final Long orderId = orderService.createOrder(orderCreateRequest);

        return ResponseEntity.ok(new OrderCreateResponse(orderId));
    }
}
