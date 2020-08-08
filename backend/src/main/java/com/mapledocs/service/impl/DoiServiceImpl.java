package com.mapledocs.service.impl;

import com.mapledocs.api.dto.external.DoiResponseDTO;
import com.mapledocs.api.dto.external.DoiServiceAuthenticateDTO;
import com.mapledocs.api.exception.DoiServiceException;
import com.mapledocs.config.DoiServiceAuthProperties;
import com.mapledocs.service.api.DoiService;
import com.mapledocs.service.external.api.DoiApiClient;
import com.mapledocs.service.external.exception.DoiApiClientException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class DoiServiceImpl implements DoiService {
    private static final Logger LOG = LoggerFactory.getLogger(DoiServiceImpl.class);
    private final DoiServiceAuthProperties doiServiceAuthProperties;
    private final DoiApiClient doiApiClient;

    public DoiServiceImpl(final DoiApiClient doiApiClient, DoiServiceAuthProperties doiServiceAuthProperties) {
        this.doiApiClient = doiApiClient;
        this.doiServiceAuthProperties = doiServiceAuthProperties;
    }

    public String getNewDoi() throws DoiServiceException {
        LOG.info("Getting new doi from doi api");
        String result = null;
        DoiResponseDTO doiResponse;
        DoiServiceAuthenticateDTO authDTO = new DoiServiceAuthenticateDTO(doiServiceAuthProperties.getUsername(),
                doiServiceAuthProperties.getPassword(), doiServiceAuthProperties.getDoiPrefix());
        try {
            doiResponse = this.doiApiClient.getNewDoi(authDTO);
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
