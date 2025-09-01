package com.gege.ideas.websocketserver.websocket.actions;

import com.fasterxml.jackson.databind.JsonNode;
import com.gege.ideas.websocketserver.message.constans.MessageConstans;
import com.gege.ideas.websocketserver.util.JsonUtil;
import com.gege.ideas.websocketserver.websocket.SessionRegistry;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;


public class PingActionService extends ActionService{

    public PingActionService(WebSocketSession session, SessionRegistry sessionRegistry) {
        super(session, sessionRegistry);
    }

    @Override
    public void handleMessage(JsonNode jsonNode) throws Exception {
        Map<String, Object> message = new HashMap<>();
        message.put("type", MessageConstans.PING);

        sendMessageBack(JsonUtil.mapToJsonString(message));

    }
}
