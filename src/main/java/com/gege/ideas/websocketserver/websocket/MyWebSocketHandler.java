package com.gege.ideas.websocketserver.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gege.ideas.websocketserver.conversation.entity.ConversationParticipant;
import com.gege.ideas.websocketserver.conversation.service.ConversationParticipantsService;
import com.gege.ideas.websocketserver.message.constans.MessageConstans;
import com.gege.ideas.websocketserver.message.entity.Message;
import com.gege.ideas.websocketserver.message.entity.MessageToSend;
import com.gege.ideas.websocketserver.message.service.MessageService;
import com.gege.ideas.websocketserver.message.service.MessageToSendService;
import com.gege.ideas.websocketserver.websocket.actions.ConnectionAction;
import com.gege.ideas.websocketserver.websocket.actions.MessageAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;


public class MyWebSocketHandler extends TextWebSocketHandler {

  private static final Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);
    private final SessionRegistry sessionRegistry = new SessionRegistry();

  @Autowired
  private ConversationParticipantsService conversationParticipantsService;

@Autowired
private MessageService messageService;

  @Autowired
private MessageToSendService messageToSendService;

  @Autowired
private ConnectionAction connectionAction;

  @Autowired
  private MessageAction messageAction;

  private String uuid;
  private String userIdString;

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

    String payload = message.getPayload();
    System.out.println(payload);
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(payload);

    int type = jsonNode.get("type").asInt();

    switch (type) {
      case MessageConstans.ARRIVAL_CONFIRMATION:
        uuid = jsonNode.has("uuid") ? jsonNode.get("uuid").asText() : null;
        userIdString = jsonNode.has("userId") ? jsonNode.get("userId").asText() : null;
        Long userId = Long.parseLong(userIdString);
        logger.info("Message from(userId): " + userIdString + ", uuid: " + uuid);
        Message messageEntity = messageService.getMessageByUuid(uuid);
        messageToSendService.markMessageAsDelivered(messageEntity.getMessageId(), userId);
        break;

      case MessageConstans.PING:
        String pongMessage = "{\"type\": \"pong\"}";
        session.sendMessage(new TextMessage(pongMessage));
        System.out.println("Ping received, responding with pong");
        break;

      case MessageConstans.MESSAGE:

        userIdString = jsonNode.has("senderId") ? jsonNode.get("senderId").asText() : null;
        String conversation = jsonNode.has("conversationId") ? jsonNode.get("conversationId").asText() : null;
        uuid = jsonNode.has("uuid") ? jsonNode.get("uuid").asText() : null;
        String timestampString = jsonNode.has("timestamp") ? jsonNode.get("timestamp").asText() : null;
        String contentEncrypted = jsonNode.has("contentEncrypted") ? jsonNode.get("contentEncrypted").asText() : null;

        logger.info("Message from(userId): " + userIdString +" Conversation: " + conversation + ", uuid: " + uuid);


        Long conversationId = Long.parseLong(conversation);
        Long senderId = Long.parseLong(userIdString);
        Long timestamp = Long.parseLong(timestampString);

        Message messageLocal = messageService.createMessage(new Message(conversationId, senderId, timestamp, contentEncrypted, MessageConstans.MESSAGE, uuid));




        List<ConversationParticipant> conversationParticipants = conversationParticipantsService.getParticipantsByConversationId(conversationId);

        for (ConversationParticipant conversationParticipant : conversationParticipants){
          if (conversationParticipant.getUserId() != senderId) {
            WebSocketSession sessionTo =
                sessionRegistry.getSession(conversationParticipant.getUserId().toString());

            if (sessionTo != null) {
              sessionTo.sendMessage(new TextMessage("Hello, " + payload + "!"));
            } else {
              messageToSendService.createMessageTo(
                  new MessageToSend(
                      messageLocal.getMessageId(), conversationParticipant.getUserId()));
              session.sendMessage(new TextMessage("{\"error\": \"User not found\"}"));
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
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
    Long userId = connectionAction.registerUser(sessionRegistry, session);
    List<Message>  messageList = messageAction.getNotDeliveredMessages(userId);
    messageAction.sendMessages(messageList, session);

    }



}