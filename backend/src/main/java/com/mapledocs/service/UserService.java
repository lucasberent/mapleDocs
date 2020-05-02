package com.mapledocs.service;

import com.mapledocs.api.dto.RegisterDTO;
import com.mapledocs.dao.UserRepository;
import com.mapledocs.domain.AppUser;
import com.mapledocs.domain.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @NotNull
    public Optional<AppUser> getUserByLogin(@Nullable final String login) {
        AppUser user = this.userRepository.findByLogin(login);
        return Optional.ofNullable(user);
    }

    @NotNull
    public void register(final RegisterDTO registerDTO) {
        this.requireNotNullOrEmpty(registerDTO);
        this.requireLoginNotExists(registerDTO.getLogin());
        userRepository.save(getAppUserFromRegisterDTO(registerDTO));
    }

    private void requireNotNullOrEmpty(final RegisterDTO registerDTO) {
        if (registerDTO == null || StringUtils.isEmpty(registerDTO.getLogin()) ||
                StringUtils.isEmpty(registerDTO.getPassword()) ||
                StringUtils.isAllBlank(registerDTO.getLogin()) || StringUtils.isBlank(registerDTO.getPassword())) {
            throw new ValidationException("DTO fields missing");
        }
    }

    private void requireLoginNotExists(final String login) {
        AppUser user = userRepository.findByLogin(login);
        if (user != null) {
            throw new ValidationException("Login for user already exists");
        }
    }

    private AppUser getAppUserFromRegisterDTO(final RegisterDTO registerDTO) {
        AppUser user = new AppUser();
        user.setLogin(registerDTO.getLogin());
        user.setRole(UserRole.ROLE_USER);
        user.setPassword(new BCryptPasswordEncoder().encode(registerDTO.getPassword()));
        user.setMaDmpSet(new ArrayList<>());
        return user;
    }
}
