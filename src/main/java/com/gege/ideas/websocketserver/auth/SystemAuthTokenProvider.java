package com.gege.ideas.websocketserver.auth;

import com.gege.ideas.websocketserver.DTO.LoginRequest;
import com.gege.ideas.websocketserver.config.ApiProperties;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SystemAuthTokenProvider {

   private final RestTemplate restTemplate;
   private String authToken;

   @Value("${system.user.email}")
   private String systemEmail;

   @Value("${system.user.password}")
   private String systemPassword;

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

      System.out.println("🔍 Attempting login to: " + loginURL);
      System.out.println("🔍 Using email: " + systemEmail);
      System.out.println(
         "🔍 Password length: " +
         (systemPassword != null ? systemPassword.length() : "null")
      );

      LoginRequest loginRequest = new LoginRequest(systemEmail, systemPassword);

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
