package com.gege.ideas.websocketserver.websocket.actions;

import com.fasterxml.jackson.databind.JsonNode;
import com.gege.ideas.websocketserver.message.service.MessageStatusService;
import com.gege.ideas.websocketserver.websocket.SessionRegistry;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class MessageStatusArriveAction extends ActionService{
    public MessageStatusArriveAction(WebSocketSession session, SessionRegistry sessionRegistry, MessageStatusService messageStatusService) throws IOException {
        super(session, sessionRegistry);
        this.messageStatusService = messageStatusService;
    }

    @Override
    public void handleMessage(JsonNode jsonNode) throws Exception {
        String uuid = jsonNode.has("uuid") ? jsonNode.get("uuid").asText() : null;
        messageStatusService.markMessageAsDelivered(uuid, authToken);
    }

    private final MessageStatusService messageStatusService;

}
