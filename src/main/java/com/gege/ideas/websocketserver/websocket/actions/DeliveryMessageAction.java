package com.gege.ideas.websocketserver.websocket.actions;

import com.fasterxml.jackson.databind.JsonNode;
import com.gege.ideas.websocketserver.message.constans.MessageConstans;
import com.gege.ideas.websocketserver.message.service.MessageStatusService;
import com.gege.ideas.websocketserver.util.JsonUtil;
import com.gege.ideas.websocketserver.websocket.SessionRegistry;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DeliveryMessageAction extends ActionService {
    public DeliveryMessageAction(WebSocketSession session, MessageStatusService messageStatusService,  SessionRegistry sessionRegistry) throws IOException {
        super(session, sessionRegistry);


        this.messageStatusService = messageStatusService;
    }

    @Override
    public void handleMessage(JsonNode jsonNode) throws Exception {
        String uuid = jsonNode.has("uuid") ? jsonNode.get("uuid").asText() : null;
        String userIdString = jsonNode.has("userId")
                ? jsonNode.get("userId").asText()
                : null;
        Long userId = Long.parseLong(userIdString);

        logger.info("Message from delivered to: " + userIdString + ", uuid: " + uuid);


        Map<String, Object> message = new HashMap<>();
        message.put("type", MessageConstans.ARRIVAL_CONFIRMATION);
        message.put("uuid", uuid);
        message.put("userId", userId);

        sendMessageToUser(JsonUtil.mapToJsonString(message), userId);
        messageStatusService.markMessageAsDelivered(uuid, authToken);
    }
    private final MessageStatusService messageStatusService;


}
