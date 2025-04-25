package com.gege.ideas.websocketserver.message.service;

import com.gege.ideas.websocketserver.conversation.service.ConversationParticipantsService;
import com.gege.ideas.websocketserver.conversation.service.ConversationService;
import com.gege.ideas.websocketserver.message.api.MessageApiClient;
import com.gege.ideas.websocketserver.message.entity.Message;
import com.gege.ideas.websocketserver.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

   private final UserService userService;
   private final ConversationParticipantsService conversationParticipantsService;

   private final ConversationService conversationService;

   private final MessageApiClient messageApiClient;

   @Autowired
   public MessageService(
      MessageApiClient messageApiClient,
      UserService userService,
      ConversationParticipantsService conversationParticipantsService,
      ConversationService conversationService
   ) {
      this.messageApiClient = messageApiClient;

      this.userService = userService;
      this.conversationParticipantsService = conversationParticipantsService;

      this.conversationService = conversationService;
   }

   public void deleteMessage(Message message, String token) {
      messageApiClient.delete(message, token);
   }

   public Message getMessageByUuid(String uuid, String token) {
      return messageApiClient.getMessage(uuid, token);
   }

   public Message addMessage(Message messageLocal, String authToken) {
      return messageApiClient.addMessage(messageLocal, authToken);
   }
}
