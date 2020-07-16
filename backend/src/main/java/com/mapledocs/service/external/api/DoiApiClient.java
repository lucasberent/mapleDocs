package com.mapledocs.service.external.api;

import com.mapledocs.api.dto.external.DoiResponseDTO;
import com.mapledocs.api.dto.external.DoiServiceAuthenticateDTO;
import com.mapledocs.api.dto.external.GetDoiRequestDTO;
import com.mapledocs.service.external.exception.DoiApiClientException;

public interface DoiApiClient {
    DoiResponseDTO getNewDoi(final GetDoiRequestDTO getDoiRequestDTO,
                             final DoiServiceAuthenticateDTO doiServiceAuthenticateDTO)
            throws DoiApiClientException;
}
