package com.gege.ideas.websocketserver.conversation.api;

import com.gege.ideas.websocketserver.DTO.ConversationDTO;
import com.gege.ideas.websocketserver.auth.SystemAuthTokenProvider;
import com.gege.ideas.websocketserver.config.ApiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ConversationApiClient {

   private final RestTemplate restTemplate;
   private final String baseUrl;
   private final SystemAuthTokenProvider authTokenProvider;

   @Autowired
   public ConversationApiClient(
      RestTemplate restTemplate,
      SystemAuthTokenProvider authTokenProvider,
      ApiProperties apiProperties
   ) {
      this.restTemplate = restTemplate;
      this.baseUrl = apiProperties.getBaseUrl() + "/conversation";
      this.authTokenProvider = authTokenProvider;
   }

   public ConversationDTO getConversation(
      Long conversationId,
      String authToken
   ) {
      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", authToken);
      HttpEntity<Void> entity = new HttpEntity<>(headers);

      try {
         ResponseEntity<ConversationDTO> response = restTemplate.exchange(
            baseUrl + "/get-conversation/" + conversationId,
            HttpMethod.GET,
            entity,
            ConversationDTO.class
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
