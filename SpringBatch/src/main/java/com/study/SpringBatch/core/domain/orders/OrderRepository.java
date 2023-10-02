package com.study.SpringBatch.core.domain.orders;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

@Configuration
public interface OrderRepository extends JpaRepository<Orders , Integer> {

}
