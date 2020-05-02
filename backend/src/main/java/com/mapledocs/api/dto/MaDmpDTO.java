package com.mapledocs.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class MaDmpDTO {
    @NotNull
    @NotEmpty
    private String json;
}
