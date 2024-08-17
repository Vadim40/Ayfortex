package com.example.techtask;

import com.example.techtask.model.Order;
import com.example.techtask.model.User;
import com.example.techtask.model.enumiration.OrderStatus;
import com.example.techtask.model.enumiration.UserStatus;
import com.example.techtask.repository.OrderRepository;
import com.example.techtask.repository.UserRepository;
import com.example.techtask.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.transaction.Transactional;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;


    @Test
    @Transactional
    public void testFindUserWithMaxTotalAmountDeliveredInYear() {

        List<User> users = userRepository.findAll();

        List<Order> orders = orderRepository.findAll();
        Map<Integer, Double> userTotalAmountMap = new HashMap<>();

        for (Order order : orders) {
            if (order.getCreatedAt().getYear() == 2003 && order.getOrderStatus() == OrderStatus.DELIVERED) {
                double totalAmount = order.getPrice() * order.getQuantity();
                userTotalAmountMap.merge(order.getUserId(), totalAmount, Double::sum);
            }
        }

        int userIdWithMaxAmount = userTotalAmountMap.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(-1);

        User expectedUser = userRepository.findById(userIdWithMaxAmount).orElse(null);


        User actualUser = userService.findUser();

        assertNotNull(expectedUser);
        assertEquals(expectedUser.getId(), actualUser.getId());
    }

    @Test
    public void testFindUsersWithPaidOrdersInYear() {
        List<User> users = userService.findUsers();
        assertNotNull(users);
        assertEquals(users.stream().findFirst().get().getUserStatus(), UserStatus.ACTIVE);
    }
}
