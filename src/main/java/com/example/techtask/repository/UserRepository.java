package com.example.techtask.repository;

import com.example.techtask.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT u.id, u.email, u.user_status " +
            "FROM users u " +
            "JOIN orders o ON u.id = o.user_id " +
            "WHERE EXTRACT(YEAR FROM o.created_at) = :year " +
            "AND o.order_status = 'DELIVERED' " +
            "GROUP BY u.id " +
            "ORDER BY SUM(o.price * o.quantity) DESC " +
            "LIMIT 1", nativeQuery = true)
    User findUserWithMaxTotalAmountDeliveredInYear(@Param("year") int year);

    @Query(value = "SELECT DISTINCT u.id, u.email, u.user_status " +
            "FROM users u " +
            "JOIN orders o ON u.id = o.user_id " +
            "WHERE EXTRACT(YEAR FROM o.created_at) = :year " +
            "AND o.order_status = 'PAID'", nativeQuery = true)
    List<User> findUsersWithPaidOrdersInYear(@Param("year") int year);
}
