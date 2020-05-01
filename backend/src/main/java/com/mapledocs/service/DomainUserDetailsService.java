package com.mapledocs.service;

import com.mapledocs.dao.UserRepository;
import com.mapledocs.domain.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Authenticate a user from the database.
 */
@Service
public class DomainUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String login) {
        AppUser user = this.userRepository.findByLogin(login);
        return this.createSpringSecurityUser(user);
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(AppUser user) {
        List<GrantedAuthority> grantedAuthorities = Arrays.asList(new SimpleGrantedAuthority(user.getRole().name().toUpperCase()));
        return new org.springframework.security.core.userdetails.User(user.getLogin(),
                user.getPassword(),
                grantedAuthorities);
    }
}
