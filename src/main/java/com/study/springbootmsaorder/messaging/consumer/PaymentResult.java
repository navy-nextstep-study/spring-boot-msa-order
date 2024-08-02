package com.study.springbootmsaorder.messaging.consumer;

public record PaymentResult(Long orderId, String status) {

}
