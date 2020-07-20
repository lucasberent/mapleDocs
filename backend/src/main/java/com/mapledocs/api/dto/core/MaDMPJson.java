package com.mapledocs.api.dto.core;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MaDMPJson {
    private Map<String, Object> dmp;
    private List<String> fieldsToHide;
}
