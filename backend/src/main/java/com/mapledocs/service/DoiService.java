package com.mapledocs.service;

import org.springframework.stereotype.Service;

@Service
public class DoiService {
    public String getNewDoi() {
        return "newTestDoi";
        /* TODO RestTemplate restTemplate = new RestTemplateBuilder().build();
        restTemplate.getForObject(Constants.DOI_SERVICE_URI, DoiResponseDTO.class); */
    }
}
