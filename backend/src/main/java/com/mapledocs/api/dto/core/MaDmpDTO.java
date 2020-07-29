package com.mapledocs.api.dto.core;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MaDmpDTO {
    public MaDmpDTO(String json) {
        this(json, null);
    }

    public MaDmpDTO(String json, Long userId) {
        this.json = json;
        this.userId = userId;
    }

    @NotNull
    @NotEmpty
    private String json;
    private Long userId;
    private String docId;
    private Boolean assignNewDoi;
    private List<String> fieldsToHide;
}
