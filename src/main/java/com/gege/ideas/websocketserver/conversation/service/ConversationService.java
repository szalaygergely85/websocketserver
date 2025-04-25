package com.gege.ideas.websocketserver.conversation.service;

import com.gege.ideas.websocketserver.DTO.ConversationDTO;
import com.gege.ideas.websocketserver.conversation.api.ConversationApiClient;
import com.gege.ideas.websocketserver.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConversationService {

   private UserService userService;
   private ConversationParticipantsService conversationParticipantsService;

   private ConversationApiClient conversationApiClient;

   @Autowired
   public ConversationService(
      UserService userService,
      ConversationParticipantsService conversationParticipantsService,
      ConversationApiClient conversationApiClient
   ) {
      this.userService = userService;
      this.conversationParticipantsService = conversationParticipantsService;
      this.conversationApiClient = conversationApiClient;
   }

   public ConversationDTO getConversation(
      Long conversationId,
      String authToken
   ) {
      return conversationApiClient.getConversation(conversationId, authToken);
   }
}
