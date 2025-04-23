package com.gege.ideas.websocketserver.message.api;

import com.gege.ideas.websocketserver.auth.SystemAuthTokenProvider;
import com.gege.ideas.websocketserver.config.ApiProperties;
import com.gege.ideas.websocketserver.message.entity.Message;
import com.gege.ideas.websocketserver.message.entity.PendingMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class PendingMessageApiClient {
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final SystemAuthTokenProvider authTokenProvider;

    @Autowired
    public PendingMessageApiClient(RestTemplate restTemplate, SystemAuthTokenProvider authTokenProvider, ApiProperties apiProperties) {
        this.restTemplate = restTemplate;
        this.baseUrl = apiProperties.getBaseUrl() + "/pending-message";
        this.authTokenProvider = authTokenProvider;
    }

    public PendingMessage addMessage(PendingMessage pendingMessage, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authToken);

        HttpEntity<PendingMessage> request = new HttpEntity<>(pendingMessage, headers);

        ResponseEntity<PendingMessage> response = restTemplate.postForEntity(
                baseUrl + "/add-message",
                request,
                PendingMessage.class
        );

        return response.getBody();
    }

    public List<PendingMessage> getNotDeliveredMessages(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(
                baseUrl + "/get-messages/not-delivered",
                HttpMethod.GET,
                request,
                List.class
        );

        return response.getBody();
    }

    public void markAsDelivered(String uuid, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken); // No need for Content-Type in GET

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + uuid + "/delivered",
                HttpMethod.GET,
                request,
                Void.class
        );

        // Optional: handle response.getStatusCode() if needed
    }



}
