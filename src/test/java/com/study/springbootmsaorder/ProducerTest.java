package com.study.springbootmsaorder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import com.study.springbootmsaorder.order.service.dto.PaymentResult;

@SpringBootTest
public class ProducerTest {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    public void test() {

        PaymentResult paymentResult = new PaymentResult(2L, "FAILED");

        kafkaTemplate.send("payment-result", paymentResult);
    }
}
