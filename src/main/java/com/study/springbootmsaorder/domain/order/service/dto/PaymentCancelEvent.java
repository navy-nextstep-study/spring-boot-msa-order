package com.study.springbootmsaorder.domain.order.service.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentCancelEvent {

    private Long orderId;
    private Long memberId;
    private Long outboxId;

    @Builder
    public PaymentCancelEvent(final Long orderId, final Long memberId) {
        this.orderId = orderId;
        this.memberId = memberId;
    }

    public void updateOutboxId(final Long outboxId) {
        this.outboxId = outboxId;
    }
}
