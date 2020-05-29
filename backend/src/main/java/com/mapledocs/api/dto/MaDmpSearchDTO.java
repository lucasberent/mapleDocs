package com.mapledocs.api.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MaDmpSearchDTO {
    public MaDmpSearchDTO(String json) {
        this();
    }

    public MaDmpSearchDTO() {

    }

    @NotNull
    @NotEmpty
    private String json;
    private Long userId;
    private String docId;

    private List<String> fieldsToHide;
}
