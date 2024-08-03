package com.study.springbootmsaorder.domain.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.springbootmsaorder.domain.order.controller.dto.OrderCancelRequest;
import com.study.springbootmsaorder.domain.order.controller.dto.OrderCreateRequest;
import com.study.springbootmsaorder.domain.order.controller.dto.OrderCreateResponse;
import com.study.springbootmsaorder.domain.order.controller.dto.OrderResponse;
import com.study.springbootmsaorder.domain.order.service.OrderService;

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

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable final Long orderId) {
        final OrderResponse orderResponse = orderService.getOrder(orderId);

        return ResponseEntity.ok(orderResponse);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable final Long orderId,
            @RequestBody final OrderCancelRequest orderCancelRequest
    ) {
        orderService.cancelOrder(orderId, orderCancelRequest);

        return ResponseEntity.noContent()
                .build();
    }
}
