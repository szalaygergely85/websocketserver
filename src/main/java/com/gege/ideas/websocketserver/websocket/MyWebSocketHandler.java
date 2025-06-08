package com.gege.ideas.websocketserver.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gege.ideas.websocketserver.DTO.ConversationDTO;
import com.gege.ideas.websocketserver.conversation.entity.ConversationParticipant;
import com.gege.ideas.websocketserver.conversation.service.ConversationService;
import com.gege.ideas.websocketserver.message.constans.MessageConstans;
import com.gege.ideas.websocketserver.message.entity.Message;
import com.gege.ideas.websocketserver.message.entity.PendingMessage;
import com.gege.ideas.websocketserver.message.service.MessageService;
import com.gege.ideas.websocketserver.message.service.PendingMessageService;
import com.gege.ideas.websocketserver.websocket.actions.ConnectionAction;
import com.gege.ideas.websocketserver.websocket.actions.MessageAction;
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

      switch (type) {
         case MessageConstans.ARRIVAL_CONFIRMATION:
            messageAction.setMessageArrived(jsonNode, authToken);
            break;
         case MessageConstans.PING:
            messageAction.answeringToPing(session);
            break;
         case MessageConstans.MESSAGE:
         case MessageConstans.IMAGE:
            Message messageLocal = messageService.addMessage(
               messageAction.jsonToMessage(jsonNode),
               authToken
            );

            ConversationDTO conversationDTO =
               conversationService.getConversation(
                  messageLocal.getConversationId(),
                  authToken
               );

            List<ConversationParticipant> conversationParticipants =
               conversationDTO.getParticipants();

            for (ConversationParticipant conversationParticipant : conversationParticipants) {
               if (
                  conversationParticipant.getUserId() !=
                  messageLocal.getSenderId()
               ) {
                  WebSocketSession sessionTo = sessionRegistry.getSession(
                     conversationParticipant.getUserId().toString()
                  );

                  if (sessionTo != null && sessionTo.isOpen()) {
                     System.out.println("Forwarding:" + message);
                     sessionTo.sendMessage(message);
                  } else {
                     pendingMessageService.addPendingMessage(
                        new PendingMessage(
                           messageLocal.getUuid(),
                           conversationParticipant.getUserId(),
                           false
                        ),
                        authToken
                     );
                  }
               }
            }
            break;
         default:
            System.out.println("Unknown message type: " + type);
            break;
      }
   }

   @Override
   public void afterConnectionEstablished(WebSocketSession session)
      throws Exception {
      super.afterConnectionEstablished(session);

      logger.info("User is connecting from: " + session.getLocalAddress());

      Long userId = connectionAction.registerUser(sessionRegistry, session);
      String token = connectionAction.getAuthToken(session);
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
   private PendingMessageService pendingMessageService;

   @Autowired
   private ConnectionAction connectionAction;

   @Autowired
   private MessageAction messageAction;

   private String uuid;
   private String userIdString;
}
