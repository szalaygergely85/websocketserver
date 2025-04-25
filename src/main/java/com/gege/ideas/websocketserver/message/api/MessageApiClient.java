package com.gege.ideas.websocketserver.message.api;

import com.gege.ideas.websocketserver.auth.SystemAuthTokenProvider;
import com.gege.ideas.websocketserver.config.ApiProperties;
import com.gege.ideas.websocketserver.message.entity.Message;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class MessageApiClient {

   private final RestTemplate restTemplate;
   private final String baseUrl;
   private final SystemAuthTokenProvider authTokenProvider;

   @Autowired
   public MessageApiClient(
      RestTemplate restTemplate,
      SystemAuthTokenProvider authTokenProvider,
      ApiProperties apiProperties
   ) {
      this.restTemplate = restTemplate;
      this.baseUrl = apiProperties.getBaseUrl() + "/message";
      this.authTokenProvider = authTokenProvider;
   }

   public Message addMessage(Message message, String authToken) {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.set("Authorization", authToken);

      HttpEntity<Message> request = new HttpEntity<>(message, headers);
      try {
         ResponseEntity<Message> response = restTemplate.postForEntity(
            baseUrl + "/add-message",
            request,
            Message.class
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

   public List<Message> getMessages(Long conversationId, String authToken) {
      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", authToken);
      HttpEntity<Void> entity = new HttpEntity<>(headers);

      ResponseEntity<List> response = restTemplate.exchange(
         baseUrl + "/get-messages?conversationId=" + conversationId,
         HttpMethod.GET,
         entity,
         List.class
      );

      return response.getBody(); // You can wrap this in a ParameterizedTypeReference for type safety
   }

   public Message getLatestMessage(Long conversationId, String authToken) {
      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", authToken);
      HttpEntity<Void> entity = new HttpEntity<>(headers);

      ResponseEntity<Message> response = restTemplate.exchange(
         baseUrl + "/get-latest-message?conversationId=" + conversationId,
         HttpMethod.GET,
         entity,
         Message.class
      );

      return response.getBody();
   }

   public Message getMessage(String uuid, String authToken) {
      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", authToken);
      HttpEntity<Void> entity = new HttpEntity<>(headers);

      ResponseEntity<Message> response = restTemplate.exchange(
         baseUrl + "/get-message/" + uuid,
         HttpMethod.GET,
         entity,
         Message.class
      );

      return response.getBody();
   }

   public void delete(Message message, String authToken) {
      //TODO create delete
   }
}
