package com.gege.ideas.websocketserver.websocket.actions;

import com.fasterxml.jackson.databind.JsonNode;
import com.gege.ideas.websocketserver.message.constans.MessageConstans;
import com.gege.ideas.websocketserver.message.entity.Message;
import com.gege.ideas.websocketserver.message.entity.MessageStatus;
import com.gege.ideas.websocketserver.util.JsonUtil;
import com.gege.ideas.websocketserver.websocket.SessionRegistry;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public abstract class ActionService {

   protected final WebSocketSession session;

   protected String authToken;

   public ActionService(
      WebSocketSession session,
      SessionRegistry sessionRegistry
   ) throws IOException {
      this.session = session;
      this.sessionRegistry = sessionRegistry;
      this.authToken = getAuthToken();
   }

   public abstract void handleMessage(JsonNode jsonNode) throws Exception;

   protected void sendMessageToSession(String message) throws IOException {
      if (session.isOpen()) {
         session.sendMessage(new TextMessage(message));
      }
   }

   protected void sendMessageToUsers(String message, List<Long> userIds)
      throws IOException {
      for (long userId : userIds) {
         sendMessageToUser(message, userId);
      }
   }

   protected void sendMessageToUser(String message, long userId)
      throws IOException {
      WebSocketSession sessionTo = sessionRegistry.getSession(
         String.valueOf(userId)
      );
      if (sessionTo != null && sessionTo.isOpen()) {
         System.out.println("Forwarding:" + message);
         sessionTo.sendMessage(new TextMessage(message));
      }
   }

   protected String getAuthToken() throws IOException {
      session.getAttributes();
      HttpHeaders headers = session.getHandshakeHeaders();
      String token = null;
      if (headers.containsKey("token") && headers.get("token") != null) {
         token = headers.getFirst("token");
      }

      if (token == null) {
         String errorMessage =
            "{\"type\": \" " +
            MessageConstans.ERROR +
            "\", \"error_type\": \" " +
            MessageConstans.ERROR_MISSING_AUTH_TOKEN +
            " \" }";

         sendMessageToSession(errorMessage);
         logger.error("Token is missing in the headers.");

         return null;
      }
      return token;
   }

   public void sendMessagesToSession(
      List<Message> messageList,
      WebSocketSession session
   ) throws IOException {
      for (Message message : messageList) {
         try {
            String messageJson = JsonUtil.objectToJson(message);
            sendMessageToSession(messageJson);
         } catch (IOException e) {
            // Log the error or handle it appropriately
            logger.error("Error sending message: " + e.getMessage());
            throw e; // Rethrow if necessary, or handle gracefully
         }
      }
   }

   public void sendMessagesStatusesToSession(
      List<MessageStatus> messageList,
      WebSocketSession session
   ) throws IOException {
      for (MessageStatus message : messageList) {
         try {
            String messageJson = JsonUtil.objectToJson(message);
            sendMessageToSession(messageJson);
         } catch (IOException e) {
            // Log the error or handle it appropriately
            logger.error("Error sending message: " + e.getMessage());
            throw e; // Rethrow if necessary, or handle gracefully
         }
      }
   }

   protected final SessionRegistry sessionRegistry;

   protected final Logger logger = LoggerFactory.getLogger(this.getClass());
}
