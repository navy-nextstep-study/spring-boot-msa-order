package com.study.springbootmsaorder.domain.order.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.springbootmsaorder.domain.order.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
