package com.mapledocs.service.external.impl;

import com.mapledocs.api.dto.external.DoiResponseDTO;
import com.mapledocs.api.dto.external.DoiServiceAuthenticateDTO;
import com.mapledocs.api.dto.external.GetDoiRequestDTO;
import com.mapledocs.service.external.api.DoiApiClient;
import com.mapledocs.service.external.exception.DoiApiClientException;
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

import java.util.HashMap;
import java.util.Map;
// https://support.datacite.org/docs/api-create-dois dto structure and api doc

@Service
@RequiredArgsConstructor
public class ZenodoApiClient implements DoiApiClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZenodoApiClient.class);

    public DoiResponseDTO getNewDoi(final DoiServiceAuthenticateDTO doiServiceAuthenticateDTO) throws DoiApiClientException {
        HttpHeaders headers = this.buildBasicHeaders(doiServiceAuthenticateDTO);
        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(buildDoiRequestDto(doiServiceAuthenticateDTO.getDoiPrefix()).getPayload(), headers);
        return this.doCreateDoiRequest(request);
    }

    private DoiResponseDTO doCreateDoiRequest(final HttpEntity<Map<String, Object>> request) throws DoiApiClientException {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        LOGGER.debug("Creating new doi with request: {} at api {}", request, Constants.DOI_SERVICE_URI);
        try {
            ResponseEntity<DoiResponseDTO> response = restTemplate
                    .postForEntity(Constants.DOI_SERVICE_URI, request, DoiResponseDTO.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new DoiApiClientException("Doi creation failed with code: " + response.getStatusCode()
                        + " and error: " + response.getBody());
            }
            return response.getBody();
        } catch (RestClientException e) {
            throw new DoiApiClientException("Doi Service Responded with :" + e.getMessage());
        }
    }

    private HttpHeaders buildBasicHeaders(final DoiServiceAuthenticateDTO doiServiceAuthenticateDTO) {
        HttpHeaders headers = new HttpHeaders();
        String authString = doiServiceAuthenticateDTO.getUsername() + ":" + doiServiceAuthenticateDTO.getPassword();
        String encoding = Base64.encode(authString.getBytes());
        headers.set("Authorization", "Basic " + encoding);
        headers.set("Content-Type", "application/vnd.api+json");
        return headers;
    }

    private GetDoiRequestDTO buildDoiRequestDto(final String doiPrefix) {
        GetDoiRequestDTO result = new GetDoiRequestDTO();
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("prefix", doiPrefix);
        data.put("type", "dois");
        data.put("attributes", attributes);
        payload.put("data", data);
        result.setPayload(payload);
        return result;
    }
}
