package com.study.springbootmsaorder.messaging.producer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Topic {
    ORDER_CANCEL("order-cancel"),
    ;

    private final String value;
}
