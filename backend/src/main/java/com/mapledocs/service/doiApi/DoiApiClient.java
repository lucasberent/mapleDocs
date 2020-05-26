package com.mapledocs.service.doiApi;

import com.mapledocs.api.dto.DoiResponseDTO;
import com.mapledocs.api.dto.GetDoiRequestDTO;
import com.mapledocs.config.DoiServiceAuthProperties;
import com.mapledocs.util.Constants;
import lombok.RequiredArgsConstructor;
import org.bson.internal.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
// https://support.datacite.org/docs/api-create-dois dto structure and api doc

@Service
@RequiredArgsConstructor
public class DoiApiClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(DoiApiClient.class);
    private final DoiServiceAuthProperties doiServiceAuthProperties;

    public DoiResponseDTO getNewDoi(final GetDoiRequestDTO getDoiRequestDTO) throws DoiApiClientException {
        HttpHeaders headers = this.buildBasicHeaders();
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(getDoiRequestDTO.getPayload(), headers);
        return this.doCreateDoiRequest(request);
    }

    private DoiResponseDTO doCreateDoiRequest(final HttpEntity<Map<String, Object>> request) throws DoiApiClientException {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        LOGGER.debug("Creating new doi with request: {} at api {}", request, Constants.DOI_SERVICE_URI);
        try {
            ResponseEntity<DoiResponseDTO> response = restTemplate
                    .postForEntity(Constants.DOI_SERVICE_URI, request, DoiResponseDTO.class);
            LOGGER.info(response.toString());
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new DoiApiClientException("Doi creation failed with code: " + response.getStatusCode()
                        + " and error: " + response.getBody());
            }
            return response.getBody();
        } catch (RestClientException e) {
            throw new DoiApiClientException("Doi Service Responded with :" + e.getMessage());
        }
    }

    private HttpHeaders buildBasicHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String authString = doiServiceAuthProperties.getUsername() + ":" + doiServiceAuthProperties.getPassword();
        String encoding = Base64.encode(authString.getBytes());
        headers.set("Authorization", "Basic " + encoding);
        headers.set("Content-Type", "application/vnd.api+json");
        return headers;
    }
}
