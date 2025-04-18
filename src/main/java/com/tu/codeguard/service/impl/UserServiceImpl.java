package com.tu.codeguard.service.impl;

import com.tu.codeguard.dto.User;
import com.tu.codeguard.repository.UserRepository;
import com.tu.codeguard.service.UserService;
import com.tu.codeguard.utils.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        log.info("Getting all users");
        return userRepository
                .findAll()
                .stream()
                .map(Mapper::mapUserToDomain)
                .toList();
    }
}
