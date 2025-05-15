package com.gege.ideas.websocketserver.user.service;

import com.gege.ideas.websocketserver.user.api.UserApiClient;
import com.gege.ideas.websocketserver.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

   private final UserApiClient userApiClient;

   @Autowired
   public UserService(UserApiClient userApiClient) {
      this.userApiClient = userApiClient;
   }

   public User getUserById(Long id) {
      return userApiClient.getUserById(id);
   }

   public User getUserByToken(String token) {
      return userApiClient.getUserByToken(token);
   }

   public Long getUserIdByToken(String token) {
      User user = getUserByToken(token);
      if (user!=null) {
         return user.getUserId();
      }
      return null;
   }
}
