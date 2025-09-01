package com.gege.ideas.websocketserver.websocket.actions;

import com.fasterxml.jackson.databind.JsonNode;
import com.gege.ideas.websocketserver.message.constans.MessageConstans;
import com.gege.ideas.websocketserver.message.entity.Message;
import com.gege.ideas.websocketserver.message.service.MessageService;
import com.gege.ideas.websocketserver.message.service.MessageStatusService;
import com.gege.ideas.websocketserver.util.JsonUtil;
import java.io.IOException;
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
   private MessageStatusService messageStatusService;
   private MessageService messageService;

   @Autowired
   public MessageAction(
      MessageStatusService messageStatusService,
      MessageService messageService
   ) {
      this.messageStatusService = messageStatusService;
      this.messageService = messageService;
   }

   public List<Message> getNotDeliveredMessages(String token) {
      List<Message> messageList = messageService.getNotDeliveredMessages(token);
      return messageList;
   }

   public void sendMessages(
      List<Message> messageList,
      WebSocketSession session
   ) throws IOException {
      for (Message message : messageList) {
         try {
            String messageJson = JsonUtil.objectToJson(message);
            session.sendMessage(new TextMessage(messageJson));
         } catch (IOException e) {
            // Log the error or handle it appropriately
            System.err.println("Error sending message: " + e.getMessage());
            throw e; // Rethrow if necessary, or handle gracefully
         }
      }
   }

   public void setMessageArrived(JsonNode jsonNode, String token) {
      String uuid = jsonNode.has("uuid") ? jsonNode.get("uuid").asText() : null;
      String userIdString = jsonNode.has("userId")
         ? jsonNode.get("userId").asText()
         : null;
      Long userId = Long.parseLong(userIdString);
      logger.info("Message from(userId): " + userIdString + ", uuid: " + uuid);

      messageStatusService.markMessageAsDelivered(uuid, token);
   }

   public void answeringToPing(WebSocketSession session) throws IOException {
      String pongMessage = "{\"type\": \" " + MessageConstans.PING + "\"}";

      //  System.out.println("Ping received, responding with pong");
      if (session.isOpen()) {
         session.sendMessage(new TextMessage(pongMessage));
      }
   }


}
