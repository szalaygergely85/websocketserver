package com.gege.ideas.websocketserver.websocket.actions;

import com.fasterxml.jackson.databind.JsonNode;
import com.gege.ideas.websocketserver.DTO.ConversationDTO;
import com.gege.ideas.websocketserver.conversation.entity.ConversationParticipant;
import com.gege.ideas.websocketserver.conversation.service.ConversationService;
import com.gege.ideas.websocketserver.message.constans.MessageConstans;
import com.gege.ideas.websocketserver.message.service.MessageStatusService;
import com.gege.ideas.websocketserver.message.service.MessageStatusType;
import com.gege.ideas.websocketserver.user.service.UserService;
import com.gege.ideas.websocketserver.util.JsonUtil;
import com.gege.ideas.websocketserver.websocket.SessionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MessageStatusUpdateAction extends ActionService{


    public MessageStatusUpdateAction(WebSocketSession session, SessionRegistry sessionRegistry, MessageStatusService messageStatusService, ConversationService conversationService) throws IOException {
        super(session, sessionRegistry);
        this.messageStatusService = messageStatusService;
        this.conversationService = conversationService;
    }

    @Override
    public void handleMessage(JsonNode jsonNode) throws Exception {
        String uuid = jsonNode.has("uuid") ? jsonNode.get("uuid").asText() : null;
        String messageStatusType = jsonNode.has("messageStatusType") ? jsonNode.get("messageStatusType").asText() : null;
        Long userId = jsonNode.has("userId") ? jsonNode.get("userId").asLong() : null;
        Long conversationId = jsonNode.has("conversationId") ? jsonNode.get("conversationId").asLong() : null;


        Map<String, Object> message = new HashMap<>();
        message.put("type", MessageConstans.MESSAGE_STATUS_UPDATE);
        message.put("conversationId", conversationId);
        message.put("messageStatusType", messageStatusType);
        message.put("uuid", uuid);
        message.put("userId", userId);

        ConversationDTO conversationDTO = conversationService.getConversation(conversationId, authToken);
        for(ConversationParticipant conversationParticipant : conversationDTO.getParticipants()){
            if(!userId.equals(conversationParticipant.getUserId())) {
                sendMessageToUser(JsonUtil.mapToJsonString(message), userId);
            }
        }
        if (messageStatusType.equals(MessageStatusType.DELIVERED)){
            messageStatusService.markMessageAsDelivered(uuid, authToken);
        }else {
        messageStatusService.markMessageAsRead(uuid, authToken);
        }
    }

    private final MessageStatusService messageStatusService;

    private final ConversationService conversationService;
}
