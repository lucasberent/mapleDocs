package com.mapledocs.api.dto.core;

import com.mapledocs.domain.UserRole;
import lombok.Data;

@Data
public class AppUserDTO {
    private String login;
    private String password;
    private UserRole userRole;
}
