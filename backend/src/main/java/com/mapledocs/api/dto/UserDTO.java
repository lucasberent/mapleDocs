package com.mapledocs.api.dto;

import com.mapledocs.domain.UserRole;
import lombok.Data;

@Data
public class UserDTO {
    private String login;
    private String password;
    private UserRole userRole;
}
