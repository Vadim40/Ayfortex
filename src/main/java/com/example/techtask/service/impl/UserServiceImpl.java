package com.example.techtask.service.impl;

import com.example.techtask.model.User;
import com.example.techtask.repository.UserRepository;
import com.example.techtask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public User findUser() {
        int year=2003;
        return userRepository.findUserWithMaxTotalAmountDeliveredInYear(year);
    }

    @Override
    public List<User> findUsers() {
        int year=2010;
        return userRepository.findUsersWithPaidOrdersInYear(year);

    }
}
