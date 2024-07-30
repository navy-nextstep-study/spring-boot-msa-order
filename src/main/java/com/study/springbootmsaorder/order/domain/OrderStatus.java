package com.study.springbootmsaorder.order.domain;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    CREATED("주문 생성"),
    COMPLETED("주문 완료"),
    FAILED("주문 실패"),
    CANCELED("주문 취소"),
    ;

    private static final Map<String, OrderStatus> orderStatusMap = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(OrderStatus::getValue, Function.identity())));

    private final String value;

    public static OrderStatus of(final String orderStatusString) {
        return orderStatusMap.get(orderStatusString);
    }
}
