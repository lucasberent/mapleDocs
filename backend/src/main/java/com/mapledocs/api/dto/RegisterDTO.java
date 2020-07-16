package com.mapledocs.api.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RegisterDTO {
    @NotNull
    @NotEmpty
    @Length(min = 5)
    private String login;
    @NotNull
    @NotEmpty
    @Length(min = 8)
    private String password;
    @Nullable
    private ExternalDoiServiceCredentialsDTO externalDoiServiceCredentialsDTO;
}
