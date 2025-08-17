package com.gege.ideas.websocketserver.notification;

import com.gege.ideas.websocketserver.auth.SystemAuthTokenProvider;
import com.gege.ideas.websocketserver.config.ApiProperties;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class NotificationApiClient {

   private final RestTemplate restTemplate;
   private final String baseUrl;
   private final SystemAuthTokenProvider authTokenProvider;

   @Autowired
   public NotificationApiClient(
      RestTemplate restTemplate,
      SystemAuthTokenProvider authTokenProvider,
      ApiProperties apiProperties
   ) {
      this.restTemplate = restTemplate;
      this.baseUrl = apiProperties.getBaseUrl() + "/notification";
      this.authTokenProvider = authTokenProvider;
   }

   public void sendNotification(
      Map<String, String> data,
      String type,
      long userId
   ) {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.set("Authorization", authTokenProvider.getAuthToken());

      String url = UriComponentsBuilder
         .fromHttpUrl(baseUrl + "/send")
         .queryParam("type", type)
         .queryParam("userId", userId)
         .toUriString();

      HttpEntity<Map<String, String>> request = new HttpEntity<>(data, headers);
      try {
         ResponseEntity<String> response = restTemplate.postForEntity(
            url,
            request,
            String.class
         );
      } catch (HttpClientErrorException | HttpServerErrorException ex) {
         System.err.println(
            "Error: " +
            ex.getStatusCode() +
            " - " +
            ex.getResponseBodyAsString()
         );
         throw ex;
      }
   }
}
