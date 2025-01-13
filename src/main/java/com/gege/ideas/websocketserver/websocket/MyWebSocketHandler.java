package com.gege.ideas.websocketserver.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gege.ideas.websocketserver.conversation.entity.ConversationParticipant;
import com.gege.ideas.websocketserver.conversation.service.ConversationParticipantsService;
import com.gege.ideas.websocketserver.message.MessageTypeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    private final SessionRegistry sessionRegistry = new SessionRegistry();

  @Autowired
  private ConversationParticipantsService conversationParticipantsService;





  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    System.out.println(message.toString());
    String payload = message.getPayload();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(payload);

    int type = jsonNode.get("type").asInt();

    switch (type) {
      case MessageTypeConstants.PING:
        String pongMessage = "{\"type\": \"pong\"}";
        session.sendMessage(new TextMessage(pongMessage));
        System.out.println("Ping received, responding with pong");
        break;

      case MessageTypeConstants.MESSAGE:
        System.out.println("message received" + jsonNode.toString());
        String senderId = jsonNode.has("senderId") ? jsonNode.get("senderId").asText() : null;
        Long conversationId = jsonNode.has("conversationId") ? jsonNode.get("conversationId").asLong() : null;
        String uuid = jsonNode.has("uuid") ? jsonNode.get("uuid").asText() : null;
        String timestamp = jsonNode.has("timestamp") ? jsonNode.get("timestamp").asText() : null;
        String content = jsonNode.has("content") ? jsonNode.get("content").asText() : null;

        List<ConversationParticipant> conversationParticipants = conversationParticipantsService.getParticipantsByConversationId(conversationId);

        for (ConversationParticipant conversationParticipant : conversationParticipants){
          WebSocketSession sessionTo = sessionRegistry.getSession(conversationParticipant.getUserId().toString());

          if (sessionTo != null) {
            sessionTo.sendMessage(new TextMessage("Hello, " + payload + "!"));
          } else {
            session.sendMessage(new TextMessage("{\"error\": \"User not found\"}"));
          }
        }
        break;

      default:
        System.out.println("Unknown message type: " + type);
        break;
    }
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        String userId = getUserIdFromSession(session);
        sessionRegistry.registerSession(userId, session);

        System.out.println(session.getUri());
    }

    private String getUserIdFromSession(WebSocketSession session) {
        session.getAttributes();
        HttpHeaders headers = session.getHandshakeHeaders();


        // Extract user ID from session, e.g., from a query parameter or header
        return headers.get("token").getFirst();
    }
}