package com.gege.ideas.websocketserver.message.service;

import com.gege.ideas.websocketserver.message.api.MessageStatusApiClient;
import com.gege.ideas.websocketserver.message.entity.MessageStatus;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageStatusService {

   private final MessageStatusApiClient messageStatusApiClient;

   @Autowired
   public MessageStatusService(MessageStatusApiClient messageStatusApiClient) {
      this.messageStatusApiClient = messageStatusApiClient;
   }

   public MessageStatus addPendingMessage(
      MessageStatus messageStatus,
      String token
   ) {
      return messageStatusApiClient.addMessage(messageStatus, token);
   }

   public void markMessageAsDelivered(String uuid, String token) {
      // Fetch the message by its ID
      messageStatusApiClient.markAsDelivered(uuid, token);
   }

   public void markMessageAsRead(String uuid, String token) {
      // Fetch the message by its ID
      messageStatusApiClient.markAsRead(uuid, token);
   }

   public void markStatusAsDelivered(String uuid, String token) {
      // Fetch the message by its ID
      messageStatusApiClient.markStatusAsDelivered(uuid, token);
   }

   public List<MessageStatus> getNotDeliveredMessages(String token) {
      return messageStatusApiClient.getNotDeliveredMessageStatus(token);
   }
}
