package com.gege.ideas.websocketserver.user.api;

import com.gege.ideas.websocketserver.auth.SystemAuthTokenProvider;
import com.gege.ideas.websocketserver.config.ApiProperties;
import com.gege.ideas.websocketserver.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserApiClient {

   private final RestTemplate restTemplate;
   private final String baseUrl;
   private final SystemAuthTokenProvider authTokenProvider;

   @Autowired
   public UserApiClient(
      RestTemplate restTemplate,
      SystemAuthTokenProvider authTokenProvider,
      ApiProperties apiProperties
   ) {
      this.restTemplate = restTemplate;
      this.baseUrl = apiProperties.getBaseUrl() + "/user";
      this.authTokenProvider = authTokenProvider;
   }

   public User getUserByToken(String token) {
      try {
         ResponseEntity<User> response = restTemplate.exchange(
            baseUrl + "/token/" + token,
            HttpMethod.GET,
            null,
            User.class
         );

         if (
            response.getStatusCode() == HttpStatus.OK &&
            response.getBody() != null
         ) {
            return response.getBody();
         } else {
            System.err.println(
               "⚠️ Failed to fetch user, status: " + response.getStatusCode()
            );
         }
      } catch (Exception e) {
         System.err.println("❌ Error fetching user: " + e.getMessage());
      }

      return null;
   }

   public User getUserById(Long id) {
      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", authTokenProvider.getAuthToken()); // <-- use system token

      HttpEntity<Void> entity = new HttpEntity<>(headers);

      try {
         ResponseEntity<User> response = restTemplate.exchange(
            baseUrl + "/id/" + id,
            HttpMethod.GET,
            entity,
            User.class
         );

         if (
            response.getStatusCode() == HttpStatus.OK &&
            response.getBody() != null
         ) {
            return response.getBody();
         } else {
            System.err.println(
               "⚠️ Failed to fetch user by ID. Status: " +
               response.getStatusCode()
            );
         }
      } catch (Exception e) {
         System.err.println("❌ Error fetching user by ID: " + e.getMessage());
      }

      return null;
   }
}
