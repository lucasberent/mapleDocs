package com.mapledocs.service;

import com.mapledocs.dao.UserRepository;
import com.mapledocs.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<AppUser> getUserByLogin(final String login) {
        AppUser user = this.userRepository.findByLogin(login);
        return Optional.ofNullable(user);
    }
}
