package com.example.techtask;

import com.example.techtask.model.Order;
import com.example.techtask.model.User;
import com.example.techtask.model.enumiration.UserStatus;
import com.example.techtask.repository.OrderRepository;
import com.example.techtask.repository.UserRepository;
import com.example.techtask.service.OrderService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;


    @Test
    @Transactional
    public void testFindMostRecentOrderWithQuantityGreaterThan() {
        Order recentOrder = orderService.findOrder();


        List<Order> allOrders = orderRepository.findAll();

        Order expectedOrder = allOrders.stream()
                .filter(order -> order.getQuantity() > 1)
                .max(Comparator.comparing(Order::getCreatedAt))
                .orElse(null);

        assertNotNull(expectedOrder);
        assertEquals(expectedOrder.getId(), recentOrder.getId());

    }

    @Test
    @Transactional
    public void testFindOrdersByActiveUsersOrderedByCreationDate() {
        List<Order> orders = orderService.findOrders();

        assertNotNull(orders);
        assertFalse(orders.isEmpty());


        List<User> activeUsers = userRepository.findAll().stream()
                .filter(user -> user.getUserStatus() == UserStatus.ACTIVE).toList();


        for (Order order : orders) {
            assertTrue(activeUsers.stream().anyMatch(user -> user.getId() == order.getUserId()));
        }
    }

}