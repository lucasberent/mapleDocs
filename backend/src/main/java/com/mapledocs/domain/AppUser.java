package com.mapledocs.domain;

import lombok.Data;
import org.apache.catalina.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
public class AppUser {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @NotNull
    private String password;
    @NotNull
    @Column(name = "login")
    private String login;
}
