package com.mapledocs.rest;

import com.mapledocs.api.dto.RegisterDTO;
import com.mapledocs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDTO registerDTO) {
        LOGGER.info("Registering user: {}", registerDTO);
        this.userService.register(registerDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
