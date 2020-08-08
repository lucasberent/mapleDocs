package com.mapledocs.service.api;

import com.mapledocs.api.exception.DoiServiceException;

public interface DoiService {
    String getNewDoi() throws DoiServiceException;
}
