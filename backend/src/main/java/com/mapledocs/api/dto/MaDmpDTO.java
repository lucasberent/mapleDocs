package com.mapledocs.api.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class MaDmpDTO {
    public MaDmpDTO(String json) {
        this.json = json;
    }

    @NotNull
    @NotEmpty
    private String json;
    private Long userId;
}
