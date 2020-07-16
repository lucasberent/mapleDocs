package com.mapledocs;

import com.mapledocs.api.dto.external.DoiResponseDTO;
import com.mapledocs.api.dto.external.DoiServiceAuthenticateDTO;
import com.mapledocs.api.dto.external.GetDoiRequestDTO;
import com.mapledocs.service.external.exception.DoiApiClientException;
import com.mapledocs.service.external.impl.ZenodoApiClient;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class ZenodoApiClientIntegrationTest {
    @Autowired
    private ZenodoApiClient zenodoApiClient;

    @Test
    public void testCreateDoiWithValidParams_shouldCreateDoi() throws DoiApiClientException {
        GetDoiRequestDTO doiRequestDTO = new GetDoiRequestDTO();
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("prefix", "10.0");
        data.put("type", "dois");
        data.put("attributes", attributes);
        payload.put("data", data);
        doiRequestDTO.setPayload(payload);
        DoiServiceAuthenticateDTO authenticateDTO =
                new DoiServiceAuthenticateDTO("testuser", "testpw", "10.0");

        DoiResponseDTO response = zenodoApiClient.getNewDoi(authenticateDTO);
        assertThat(response).isNotNull();
        assertThat(response.getData().containsKey("id"));
    }
}
