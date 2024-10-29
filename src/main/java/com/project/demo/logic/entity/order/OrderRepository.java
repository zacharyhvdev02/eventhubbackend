package com.project.demo.logic.entity.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // @Query("SELECT o FROM Order o WHERE o.user.id = ?3")
    // List<Order> getOrderByUserId(Long userId);

    Page<Order> getOrderByUserId(Long userId, Pageable pageable);
}
