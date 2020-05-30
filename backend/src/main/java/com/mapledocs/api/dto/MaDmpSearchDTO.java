package com.mapledocs.api.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MaDmpSearchDTO {
    private List<String> docIds;
}
