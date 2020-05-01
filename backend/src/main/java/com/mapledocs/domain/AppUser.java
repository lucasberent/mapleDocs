package com.mapledocs.domain;

import lombok.Data;

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
    @OneToMany(fetch = FetchType.LAZY)
    private List<UserMaDmaps> maDmpSet;
}
