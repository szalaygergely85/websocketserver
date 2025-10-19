package com.gege.ideas.websocketserver.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gege.ideas.websocketserver.conversation.service.ConversationService;
import com.gege.ideas.websocketserver.message.constans.MessageConstans;
import com.gege.ideas.websocketserver.message.service.MessageService;
import com.gege.ideas.websocketserver.message.service.MessageStatusService;
import com.gege.ideas.websocketserver.user.service.UserService;
import com.gege.ideas.websocketserver.websocket.actions.*;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyWebSocketHandler extends TextWebSocketHandler {

   @Override
   public void handleTextMessage(WebSocketSession session, TextMessage message)
      throws Exception {
      String payload = message.getPayload();
      logger.info("Payload arrived " + payload);
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(payload);

      int type = jsonNode.get("type").asInt();

      ActionService service =
         switch (type) {
            case MessageConstans.MESSAGE_STATUS -> new MessageStatusAction(
               session,
               messageStatusService,
               sessionRegistry
            );
            case MessageConstans.MESSAGE_STATUS_UPDATE -> new MessageStatusUpdateAction(
               session,
               sessionRegistry,
               messageStatusService,
               conversationService
            );
            case MessageConstans.MESSAGE_STATUS_ARRIVED -> new MessageStatusArriveAction(
               session,
               sessionRegistry,
               messageStatusService
            );
            case MessageConstans.PING -> new PingActionService(
               session,
               sessionRegistry
            );
            case MessageConstans.MESSAGE,
               MessageConstans.IMAGE -> new MediaMessageAction(
               session,
               conversationService,
               messageService,
               sessionRegistry
            );
            default -> null;
         };

      if (service != null) {
         service.handleMessage(jsonNode);
      }
   }

   @Override
   public void afterConnectionEstablished(WebSocketSession session)
      throws Exception {
      super.afterConnectionEstablished(session);

      ConnectionAction connectionAction = new ConnectionAction(
         session,
         sessionRegistry,
         userService,
         messageService,
         messageStatusService
      );
      connectionAction.registerUser();
   }

   @Override
   public void afterConnectionClosed(
      WebSocketSession session,
      org.springframework.web.socket.CloseStatus status
   ) throws Exception {
      super.afterConnectionClosed(session, status);

      // Remove session from registry to prevent memory leak
      String userId = null;
      for (Map.Entry<String, WebSocketSession> entry : sessionRegistry
         .getAllSessions()
         .entrySet()) {
         if (entry.getValue().getId().equals(session.getId())) {
            userId = entry.getKey();
            break;
         }
      }

      if (userId != null) {
         sessionRegistry.removeSession(userId);
         logger.info("User {} disconnected and session removed", userId);
      }
   }

   private static final Logger logger = LoggerFactory.getLogger(
      MyWebSocketHandler.class
   );
   private final SessionRegistry sessionRegistry = new SessionRegistry();

   @Autowired
   private ConversationService conversationService;

   @Autowired
   private MessageService messageService;

   @Autowired
   private MessageStatusService messageStatusService;

   @Autowired
   private UserService userService;
}
