package com.gege.ideas.websocketserver.message.service;

import com.gege.ideas.websocketserver.message.api.PendingMessageApiClient;
import com.gege.ideas.websocketserver.message.entity.PendingMessage;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PendingMessageService {

   private final PendingMessageApiClient pendingMessageApiClient;

   @Autowired
   public PendingMessageService(
      PendingMessageApiClient pendingMessageApiClient
   ) {
      this.pendingMessageApiClient = pendingMessageApiClient;
   }

   public PendingMessage addPendingMessage(
      PendingMessage pendingMessage,
      String token
   ) {
      return pendingMessageApiClient.addMessage(pendingMessage, token);
   }

   public List<PendingMessage> getNotDeliveredMessages(String token) {
      return pendingMessageApiClient.getNotDeliveredMessages(token);
   }

   public void markMessageAsDelivered(String uuid, String token) {
      // Fetch the message by its ID
      pendingMessageApiClient.markAsDelivered(uuid, token);
   }
}
