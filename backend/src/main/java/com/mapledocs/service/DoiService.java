package com.mapledocs.service;

import com.mapledocs.api.dto.DoiResponseDTO;
import com.mapledocs.api.dto.GetDoiRequestDTO;
import com.mapledocs.api.exception.DoiServiceException;
import com.mapledocs.service.doiApi.DoiApiClient;
import com.mapledocs.service.doiApi.DoiApiClientException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Getter
@Setter
public class DoiService {
    private static final Logger LOG = LoggerFactory.getLogger(DoiService.class);

    private final DoiApiClient doiApiClient;
    private final GsonJsonParser parser;

    public DoiService(final DoiApiClient doiApiClient) {
        this.parser = new GsonJsonParser();
        this.doiApiClient = doiApiClient;
    }

    public String getNewDoi(final GetDoiRequestDTO getDoiRequestDTO) throws DoiServiceException {
        String result = null;
        DoiResponseDTO doiResponseDTO = null;
        try {
            doiResponseDTO = this.doiApiClient.getNewDoi(getDoiRequestDTO);
            return this.getDoiStringFromDTO(doiResponseDTO);
        } catch (DoiApiClientException e) {
            throw new DoiServiceException(e.getMessage());
        }
    }

    private String getDoiStringFromDTO(final DoiResponseDTO doiResponseDTO) throws DoiServiceException {
        String result = null;
        LOG.info(doiResponseDTO.toString());
        LOG.info(doiResponseDTO.getResponsePayload());
        Map<String, Object> jsonPayload = parser.parseMap(doiResponseDTO.getResponsePayload());
        this.validateResponsePayload(jsonPayload);
        Map<String, Object> data = parser.parseMap(jsonPayload.get("data").toString());
        if (data != null) {
            if (data.containsKey("id")) {
                result = data.get("id").toString();
            }
        }
        return result;
    }

    private void validateResponsePayload(Map<String, Object> jsonPayload) throws DoiServiceException {
        if (jsonPayload == null) {
            throw new DoiServiceException("Received Empty payload instead of created doi response");
        }
        Map<String, Object> data = parser.parseMap(jsonPayload.get("data").toString());
        if (data == null || !data.containsKey("id") || data.get("id") == null || data.get("id").toString().isEmpty()) {
            throw new DoiServiceException("Received payload does not contain doi");
        }
    }
}
