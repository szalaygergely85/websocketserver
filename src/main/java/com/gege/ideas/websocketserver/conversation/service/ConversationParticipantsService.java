package com.gege.ideas.websocketserver.conversation.service;

import com.gege.ideas.websocketserver.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConversationParticipantsService {

   private UserService userService;

   @Autowired
   public ConversationParticipantsService(UserService userService) {
      this.userService = userService;
   }
}
