package com.study.springbootmsaorder.domain.order.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.springbootmsaorder.domain.order.domain.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}
