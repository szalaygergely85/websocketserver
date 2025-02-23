package com.gege.ideas.websocketserver.websocket.actions;

import com.fasterxml.jackson.databind.JsonNode;
import com.gege.ideas.websocketserver.message.constans.MessageConstans;
import com.gege.ideas.websocketserver.message.entity.Message;
import com.gege.ideas.websocketserver.message.entity.MessageToSend;
import com.gege.ideas.websocketserver.message.service.MessageService;
import com.gege.ideas.websocketserver.message.service.MessageToSendService;
import com.gege.ideas.websocketserver.util.JsonUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class MessageAction {

   private static final Logger logger = LoggerFactory.getLogger(
      MessageAction.class
   );
   private MessageToSendService messageToSendService;
   private MessageService messageService;

   @Autowired
   public MessageAction(
      MessageToSendService messageToSendService,
      MessageService messageService
   ) {
      this.messageToSendService = messageToSendService;
      this.messageService = messageService;
   }

   public List<Message> getNotDeliveredMessages(Long userId) {
      List<MessageToSend> messagesNotDelivered =
         messageToSendService.getNotDeliveredMessages(userId);
      List<Message> messageList = new ArrayList<>();
      for (MessageToSend messageNotDelivered : messagesNotDelivered) {
         Message message = messageService.getMessageById(
            messageNotDelivered.getMessageId()
         );
         if (message != null) {
            messageList.add(message);
         }
      }
      return messageList;
   }

   public void sendMessages(
      List<Message> messageList,
      WebSocketSession session
   ) throws IOException {
      for (Message message : messageList) {
         try {
            String messageJson = JsonUtil.toJson(message);
            session.sendMessage(new TextMessage(messageJson));
         } catch (IOException e) {
            // Log the error or handle it appropriately
            System.err.println("Error sending message: " + e.getMessage());
            throw e; // Rethrow if necessary, or handle gracefully
         }
      }
   }

   public void setMessageArrived(JsonNode jsonNode) {
      String uuid = jsonNode.has("uuid") ? jsonNode.get("uuid").asText() : null;
      String userIdString = jsonNode.has("userId")
         ? jsonNode.get("userId").asText()
         : null;
      Long userId = Long.parseLong(userIdString);
      logger.info("Message from(userId): " + userIdString + ", uuid: " + uuid);
      Message messageEntity = messageService.getMessageByUuid(uuid);
      messageToSendService.markMessageAsDelivered(
         messageEntity.getMessageId(),
         userId
      );
   }

   public void answeringToPing(WebSocketSession session) throws IOException {
      String pongMessage = "{\"type\": \"pong\"}";

      //  System.out.println("Ping received, responding with pong");
    if (session.isOpen()) {
      session.sendMessage(new TextMessage(pongMessage));
      }
   }

   public Message saveJsonToMessage(JsonNode jsonNode) {
      String userIdString = jsonNode.has("senderId")
         ? jsonNode.get("senderId").asText()
         : null;
      String conversation = jsonNode.has("conversationId")
         ? jsonNode.get("conversationId").asText()
         : null;
      String uuid = jsonNode.has("uuid") ? jsonNode.get("uuid").asText() : null;
      String timestampString = jsonNode.has("timestamp")
         ? jsonNode.get("timestamp").asText()
         : null;
      String contentEncrypted = jsonNode.has("contentEncrypted")
         ? jsonNode.get("contentEncrypted").asText()
         : null;

      logger.info(
         "Message from(userId): " +
         userIdString +
         " Conversation: " +
         conversation +
         ", uuid: " +
         uuid
      );

      Long conversationId = Long.parseLong(conversation);
      Long senderId = Long.parseLong(userIdString);
      Long timestamp = Long.parseLong(timestampString);

      return messageService.createMessage(
         new Message(
            conversationId,
            senderId,
            timestamp,
            contentEncrypted,
            MessageConstans.MESSAGE,
            uuid
         )
      );
   }
}
