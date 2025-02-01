package com.gege.ideas.websocketserver.user.service;

import com.gege.ideas.websocketserver.user.entity.UserToken;
import com.gege.ideas.websocketserver.user.repository.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTokenService {

   private final UserTokenRepository userTokenRepository;

   @Autowired
   public UserTokenService(UserTokenRepository userTokenRepository) {
      this.userTokenRepository = userTokenRepository;
   }

   public UserToken getUserTokenByTokenId(Long id) {
      return userTokenRepository.findByUserTokenId(id);
   }

   public UserToken getUserTokenByToken(String token) {
      return userTokenRepository.findUserTokenByToken(token);
   }
}
