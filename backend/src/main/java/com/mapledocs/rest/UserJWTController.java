package com.mapledocs.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mapledocs.api.dto.core.LoginDTO;
import com.mapledocs.api.exception.rest.NotFoundException;
import com.mapledocs.api.exception.rest.UnauthorizedException;
import com.mapledocs.domain.AppUser;
import com.mapledocs.security.jwt.JWTFilter;
import com.mapledocs.security.jwt.TokenProvider;
import com.mapledocs.service.api.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/authentication")
public class UserJWTController {
    private final TokenProvider tokenProvider;

    private final UserService userService;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserJWTController(TokenProvider tokenProvider,
                             AuthenticationManagerBuilder authenticationManagerBuilder,
                             UserService userService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<JWTToken> authenticate(@Valid @RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getLogin(), loginDTO.getPassword());
        Authentication authentication;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("INVALID_PASSWORD " + e.getMessage());
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication);
        System.out.println("token: " + jwt);
        AppUser appUser = userService.getUserByLogin(loginDTO.getLogin()).orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt, appUser.getLogin()), httpHeaders, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(authHeader.indexOf("Bearer "));
        this.tokenProvider.validateToken(token);
        return ResponseEntity.ok(this.tokenProvider.createRefreshToken(this.resolveToken(token)));
    }

    private String resolveToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    static class JWTToken {

        private String payload;
        private final String login;

        JWTToken(String payload, String login) {
            this.payload = payload;
            this.login = login;
        }

        @JsonProperty("payload")
        String getPayload() {
            return payload;
        }

        @JsonProperty("login")
        String getLogin() {
            return login;
        }

        void setPayload(String payload) {
            this.payload = payload;
        }
    }
}
