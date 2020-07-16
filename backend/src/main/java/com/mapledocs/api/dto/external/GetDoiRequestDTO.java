package com.mapledocs.api.dto.external;

import lombok.Data;

import java.util.Map;

@Data
public class GetDoiRequestDTO {
    private Map<String, Object> payload;
}
