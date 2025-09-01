package com.gege.ideas.websocketserver.websocket.actions;

import com.fasterxml.jackson.databind.JsonNode;
import com.gege.ideas.websocketserver.message.constans.MessageConstans;
import com.gege.ideas.websocketserver.message.service.MessageStatusService;
import com.gege.ideas.websocketserver.util.JsonUtil;
import com.gege.ideas.websocketserver.websocket.SessionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

public class ReadMessageAction extends ActionService {
    public ReadMessageAction(WebSocketSession session, MessageStatusService messageStatusService, String token, SessionRegistry sessionRegistry) {
        super(session,sessionRegistry);

        this.token= token;
        this.messageStatusService = messageStatusService;
    }

    @Override
    public void handleMessage(JsonNode jsonNode) throws Exception {
        String uuid = jsonNode.has("uuid") ? jsonNode.get("uuid").asText() : null;
        String userIdString = jsonNode.has("userId")
                ? jsonNode.get("userId").asText()
                : null;
        Long userId = Long.parseLong(userIdString);

        logger.info("Message from read by: " + userIdString + ", uuid: " + uuid);


        Map<String, Object> message = new HashMap<>();
        message.put("type", MessageConstans.READ_CONFIRMATION);
        message.put("uuid", uuid);
        message.put("userId", userId);

        sendMessageToUser(JsonUtil.mapToJsonString(message), userId);
        messageStatusService.markMessageAsRead(uuid, token);
    }

    private static final Logger logger = LoggerFactory.getLogger(
            ReadMessageAction.class
    );
    private final MessageStatusService messageStatusService;

    private final String token;
}
