package com.mapledocs.service;

import com.mapledocs.api.dto.DoiResponseDTO;
import com.mapledocs.api.dto.GetDoiRequestDTO;
import com.mapledocs.api.dto.ZenodoCredentialsDTO;
import com.mapledocs.api.exception.DoiServiceException;
import com.mapledocs.domain.ExternalDoiServiceCredentials;
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

    public String getNewDoi(final GetDoiRequestDTO getDoiRequestDTO, final ZenodoCredentialsDTO zenodoCredentialsDTO) throws DoiServiceException {
        String result = null;
        DoiResponseDTO doiResponse = null;
        try {
            doiResponse = this.doiApiClient.getNewDoi(getDoiRequestDTO, zenodoCredentialsDTO);
            return this.getDoiStringFromJSONResponse(doiResponse);
        } catch (DoiApiClientException e) {
            throw new DoiServiceException(e.getMessage());
        }
    }

    private String getDoiStringFromJSONResponse(final DoiResponseDTO doiResponse) throws DoiServiceException {
        if (doiResponse.getData() != null) {
            Object id = doiResponse.getData().get("id");
            if (id instanceof String) {
                return (String) doiResponse.getData().get("id");
            }
            else {
                throw new DoiServiceException("Received payload does not contain doi");
            }
        }
        else {
            throw new DoiServiceException("Received Empty payload instead of created doi response");
        }
    }
}
