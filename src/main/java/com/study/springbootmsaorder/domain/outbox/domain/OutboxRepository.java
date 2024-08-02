package com.study.springbootmsaorder.domain.outbox.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {

    List<Outbox> findByEventStatus(final EventStatus eventStatus);
}
