package com.mapledocs.service.impl;

import com.mapledocs.api.dto.external.DoiResponseDTO;
import com.mapledocs.api.dto.external.DoiServiceAuthenticateDTO;
import com.mapledocs.api.exception.DoiServiceException;
import com.mapledocs.service.api.DoiService;
import com.mapledocs.service.external.api.DoiApiClient;
import com.mapledocs.service.external.exception.DoiApiClientException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class DoiServiceImpl implements DoiService {
    private static final Logger LOG = LoggerFactory.getLogger(DoiServiceImpl.class);

    private final DoiApiClient doiApiClient;
    private final GsonJsonParser parser;

    public DoiServiceImpl(final DoiApiClient doiApiClient) {
        this.parser = new GsonJsonParser();
        this.doiApiClient = doiApiClient;
    }

    public String getNewDoi(final DoiServiceAuthenticateDTO doiServiceAuthenticateDTO) throws DoiServiceException {
        LOG.info("Getting new doi from doi api");
        String result = null;
        DoiResponseDTO doiResponse;
        try {
            doiResponse = this.doiApiClient.getNewDoi(doiServiceAuthenticateDTO);
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
            } else {
                throw new DoiServiceException("Received payload does not contain doi");
            }
        } else {
            throw new DoiServiceException("Received Empty payload instead of created doi response");
        }
    }
}
