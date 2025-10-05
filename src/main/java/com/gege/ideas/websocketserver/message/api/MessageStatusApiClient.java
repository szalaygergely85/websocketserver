package com.gege.ideas.websocketserver.message.api;

import com.gege.ideas.websocketserver.auth.SystemAuthTokenProvider;
import com.gege.ideas.websocketserver.config.ApiProperties;
import com.gege.ideas.websocketserver.message.entity.MessageStatus;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class MessageStatusApiClient {

   private final RestTemplate restTemplate;
   private final String baseUrl;
   private final SystemAuthTokenProvider authTokenProvider;

   @Autowired
   public MessageStatusApiClient(
      RestTemplate restTemplate,
      SystemAuthTokenProvider authTokenProvider,
      ApiProperties apiProperties
   ) {
      this.restTemplate = restTemplate;
      this.baseUrl = apiProperties.getBaseUrl() + "/message-status";
      this.authTokenProvider = authTokenProvider;
   }

   public MessageStatus addMessage(
      MessageStatus messageStatus,
      String authToken
   ) {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.set("Authorization", authToken);
      try {
         HttpEntity<MessageStatus> request = new HttpEntity<>(
            messageStatus,
            headers
         );

         ResponseEntity<MessageStatus> response = restTemplate.postForEntity(
            baseUrl + "/add",
            request,
            MessageStatus.class
         );

         return response.getBody();
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

   public void markAsDelivered(String uuid, String authToken) {
      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", authToken); // No need for Content-Type in GET

      HttpEntity<Void> request = new HttpEntity<>(headers);
      try {
         ResponseEntity<Void> response = restTemplate.exchange(
            baseUrl + "/" + uuid + "/delivered",
            HttpMethod.POST,
            request,
            Void.class
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
      // Optional: handle response.getStatusCode() if needed
   }

   public void markAsRead(String uuid, String authToken) {
      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", authToken); // No need for Content-Type in GET

      HttpEntity<Void> request = new HttpEntity<>(headers);
      try {
         ResponseEntity<Void> response = restTemplate.exchange(
            baseUrl + "/" + uuid + "/read",
            HttpMethod.POST,
            request,
            Void.class
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
      // Optional: handle response.getStatusCode() if needed
   }

   public void markStatusAsDelivered(String uuid, String authToken) {
      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", authToken); // No need for Content-Type in GET

      HttpEntity<Void> request = new HttpEntity<>(headers);
      try {
         ResponseEntity<Void> response = restTemplate.exchange(
                 baseUrl + "/" + uuid + "/status-delivered",
                 HttpMethod.POST,
                 request,
                 Void.class
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
      // Optional: handle response.getStatusCode() if needed
   }

   public List<MessageStatus> getNotDeliveredMessageStatus(String authToken) {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.set("Authorization", authToken);

      try {
         HttpEntity<Void> request = new HttpEntity<>(headers);

         ResponseEntity<List<MessageStatus>> response = restTemplate.exchange(
            baseUrl + "/get-messages-status/not-delivered",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<List<MessageStatus>>() {}
         );

         return response.getBody();
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
