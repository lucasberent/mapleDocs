package com.mapledocs.api.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ExternalDoiServiceCredentialsDTO {
    @NotNull
    @NotEmpty
    private String username;
    @NotNull
    @NotEmpty
    private String doiPrefix;
}
