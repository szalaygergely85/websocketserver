package com.gege.ideas.websocketserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiProperties {

    @Value("${rest.api.base-url}")
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }
}
