package com.gege.ideas.websocketserver.message.service;

import com.gege.ideas.websocketserver.conversation.service.ConversationParticipantsService;
import com.gege.ideas.websocketserver.conversation.service.ConversationService;
import com.gege.ideas.websocketserver.message.api.MessageApiClient;
import com.gege.ideas.websocketserver.message.entity.Message;
import com.gege.ideas.websocketserver.user.service.UserService;
import java.util.List;
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

   public Message getMessageByUuid(String uuid, String token) {
      return messageApiClient.getMessage(uuid, token);
   }

   public Message addMessage(Message message, String authToken) {
      return messageApiClient.addMessage(message, authToken);
   }

   public List<Message> getNotDeliveredMessages(String token) {
      return messageApiClient.getNotDeliveredMessages(token);
   }
}
