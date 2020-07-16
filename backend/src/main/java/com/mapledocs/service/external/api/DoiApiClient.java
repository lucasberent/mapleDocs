package com.mapledocs.service.external.api;

import com.mapledocs.api.dto.external.DoiResponseDTO;
import com.mapledocs.api.dto.external.DoiServiceAuthenticateDTO;
import com.mapledocs.service.external.exception.DoiApiClientException;

public interface DoiApiClient {
    DoiResponseDTO getNewDoi(final DoiServiceAuthenticateDTO doiServiceAuthenticateDTO)
            throws DoiApiClientException;
}
