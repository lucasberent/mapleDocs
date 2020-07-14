package com.mapledocs;

import com.mapledocs.api.dto.DoiResponseDTO;
import com.mapledocs.api.dto.GetDoiRequestDTO;
import com.mapledocs.config.DoiServiceAuthProperties;
import com.mapledocs.service.doiApi.DoiApiClient;
import com.mapledocs.service.doiApi.DoiApiClientException;
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
class DoiApiClientIntegrationTest {
    @Autowired
    private DoiApiClient doiApiClient;
    @Autowired
    private DoiServiceAuthProperties doiServiceAuthProperties;

    @Test
    public void testCreateDoiWithValidParams_shouldCreateDoi() throws DoiApiClientException {
        GetDoiRequestDTO doiRequestDTO = new GetDoiRequestDTO();
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("prefix", doiServiceAuthProperties.getDoiPrefix());
        data.put("type", "dois");
        data.put("attributes", attributes);
        payload.put("data", data);
        doiRequestDTO.setPayload(payload);

        DoiResponseDTO response = doiApiClient.getNewDoi(doiRequestDTO);
        assertThat(response).isNotNull();
        assertThat(response.getData().containsKey("id"));
    }
}
