package com.gege.ideas.websocketserver.message.service;

import com.gege.ideas.websocketserver.message.api.MessageStatusApiClient;
import com.gege.ideas.websocketserver.message.entity.PendingMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageStatusService {

   private final MessageStatusApiClient messageStatusApiClient;

   @Autowired
   public MessageStatusService(
      MessageStatusApiClient messageStatusApiClient
   ) {
      this.messageStatusApiClient = messageStatusApiClient;
   }

   public PendingMessage addPendingMessage(
      PendingMessage pendingMessage,
      String token
   ) {
      return messageStatusApiClient.addMessage(pendingMessage, token);
   }

   public void markMessageAsDelivered(String uuid, String token) {
      // Fetch the message by its ID
      messageStatusApiClient.markAsDelivered(uuid, token);
   }

   public void markMessageAsRead(String uuid, String token) {
      // Fetch the message by its ID
      messageStatusApiClient.markAsRead(uuid, token);
   }
}
