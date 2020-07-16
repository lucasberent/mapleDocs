package com.mapledocs.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class ExternalDoiServiceCredentials {
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    private String username;
    @NotNull
    private String doiPrefix;
}
