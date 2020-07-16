package com.mapledocs.service.api;

import com.mapledocs.api.dto.external.DoiServiceAuthenticateDTO;
import com.mapledocs.api.exception.DoiServiceException;

public interface DoiService {
    String getNewDoi(final DoiServiceAuthenticateDTO doiServiceAuthenticateDTO) throws DoiServiceException;
}
