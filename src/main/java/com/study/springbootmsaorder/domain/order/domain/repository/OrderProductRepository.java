package com.study.springbootmsaorder.domain.order.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.springbootmsaorder.domain.order.domain.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    List<OrderProduct> findByOrderId(final Long id);
}
