package com.gege.ideas.websocketserver.auth;

import com.gege.ideas.websocketserver.DTO.LoginRequest;
import com.gege.ideas.websocketserver.config.ApiProperties;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SystemAuthTokenProvider {

   private final RestTemplate restTemplate;
   private String authToken;

   private static final String SYSTEM_EMAIL = "websocket@zenvy.com";
   private static final String SYSTEM_PASSWORD = "123456";

   private final ApiProperties apiProperties;

   @Autowired
   public SystemAuthTokenProvider(
      RestTemplate restTemplate,
      ApiProperties apiProperties
   ) {
      this.restTemplate = restTemplate;
      this.apiProperties = apiProperties;
   }

   @PostConstruct
   public void loginAtStartup() {
      String loginURL = apiProperties.getBaseUrl() + "/user/login";

      LoginRequest loginRequest = new LoginRequest(
         SYSTEM_EMAIL,
         SYSTEM_PASSWORD
      );

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      HttpEntity<LoginRequest> request = new HttpEntity<>(
         loginRequest,
         headers
      );

      try {
         ResponseEntity<String> response = restTemplate.postForEntity(
            loginURL,
            request,
            String.class
         );

         if (
            response.getStatusCode() == HttpStatus.OK &&
            response.getBody() != null
         ) {
            // Assuming response body contains just the token string — adjust if it's JSON
            this.authToken = "Bearer " + response.getBody().trim();
            System.out.println("✅ System login successful. Token acquired.");
         } else {
            System.err.println("❌ Login failed: " + response.getStatusCode());
         }
      } catch (Exception e) {
         System.err.println("❌ System login error: " + e.getMessage());
      }
   }

   public String getAuthToken() {
      return authToken;
   }
}
