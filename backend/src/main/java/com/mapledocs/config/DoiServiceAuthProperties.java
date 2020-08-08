package com.mapledocs.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Configuration
@PropertySource("classpath:auth.properties")
@Service
@Data
public class DoiServiceAuthProperties {
    @Value("${application.rest.doiservice.username}")
    private String username;
    @Value("${application.rest.doiservice.password}")
    private String password;
    @Value("${application.rest.doiservice.prefix}")
    private String doiPrefix;

}
