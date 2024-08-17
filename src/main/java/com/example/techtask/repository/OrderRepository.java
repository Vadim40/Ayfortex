package com.example.techtask.repository;

import com.example.techtask.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "SELECT * FROM orders " +
            "WHERE quantity > :quantity " +
            "ORDER BY created_at DESC " +
            "LIMIT 1", nativeQuery = true)
    Order findMostRecentOrderWithQuantityGreaterThan(@Param("quantity") int quantity);

    @Query(value = "SELECT o.* FROM orders o " +
            "JOIN users u ON o.user_id = u.id " +
            "WHERE u.user_status = 'ACTIVE' " +
            "ORDER BY o.created_at ASC", nativeQuery = true)
    List<Order> findOrdersByActiveUsersOrderedByCreationDate();
}
