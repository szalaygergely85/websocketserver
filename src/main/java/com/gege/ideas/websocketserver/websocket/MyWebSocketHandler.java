package com.gege.ideas.websocketserver.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gege.ideas.websocketserver.conversation.service.ConversationService;
import com.gege.ideas.websocketserver.message.constans.MessageConstans;
import com.gege.ideas.websocketserver.message.entity.Message;
import com.gege.ideas.websocketserver.message.service.MessageService;
import com.gege.ideas.websocketserver.message.service.MessageStatusService;
import com.gege.ideas.websocketserver.websocket.actions.*;

import java.util.List;

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
      String authToken = connectionAction.getAuthToken(session);

      String payload = message.getPayload();
      System.out.println(payload);
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(payload);

      int type = jsonNode.get("type").asInt();

      ActionService service = switch (type) {

         case MessageConstans.READ_CONFIRMATION -> new ReadMessageAction(session, messageStatusService, authToken, sessionRegistry);
         case MessageConstans.ARRIVAL_CONFIRMATION -> new DeliveryMessageAction(session, messageStatusService, authToken, sessionRegistry);
         case MessageConstans.PING -> new PingActionService(session, sessionRegistry);
         case MessageConstans.MESSAGE, MessageConstans.IMAGE -> new MediaMessageAction(session, conversationService, messageService,authToken, sessionRegistry);
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



      Long userId = connectionAction.registerUser(sessionRegistry, session);
      String token = connectionAction.getAuthToken(session);

      logger.info("User {} is connected with {}.", userId, token);
      if (userId != null) {
         List<Message> messageList = messageAction.getNotDeliveredMessages(
            token
         );
         messageAction.sendMessages(messageList, session);
      } else {
         session.close();
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
   private ConnectionAction connectionAction;

   @Autowired
   private MessageAction messageAction;

}
