package com.mapledocs.api.dto.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapledocs.api.exception.rest.MaDmpServiceCreationException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MaDMPMap {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(MaDMPMap.class);
    private Map<String, Object> dmp;
    private List<String> fieldsToHide;
    private String mongoId;
    private String fulltextString;

    public static String toJsonString(final MaDMPMap maDMPMap) {
        try {
            return mapper.writeValueAsString(maDMPMap);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error parsing MaDMPJson to JsonString");
            throw new MaDmpServiceCreationException("Error parsing MaDMPJson to JsonString");
        }
    }

    public static MaDMPMap fromJsonString(final String json) {
        try {
            return mapper.readValue(json, MaDMPMap.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error parsing maDMP {}", json);
            throw new MaDmpServiceCreationException("Error parsing maDMP: " + e.getMessage());
        }
    }
}
