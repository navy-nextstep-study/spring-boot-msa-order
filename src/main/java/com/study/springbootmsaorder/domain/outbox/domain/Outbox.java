package com.study.springbootmsaorder.domain.outbox.domain;

import com.study.springbootmsaorder.global.domain.BaseEntity;
import com.study.springbootmsaorder.messaging.producer.Topic;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Outbox extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AggregateType aggregateType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

    @Column(nullable = false)
    private Long retries;

    @Column(nullable = false)
    private String payload;

    @Column(nullable = false)
    private String payloadType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Topic topic;

    @Builder
    public Outbox(
            final AggregateType aggregateType,
            final EventType eventType,
            final String payload,
            final String payloadType,
            final Topic topic
    ) {
        this.aggregateType = aggregateType;
        this.eventType = eventType;
        this.eventStatus = EventStatus.FAILED;
        this.retries = 0L;
        this.payload = payload;
        this.payloadType = payloadType;
        this.topic = topic;
    }

    public void updateEventStatus(final EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public void increaseRetryCount() {
        this.retries++;
    }
}
