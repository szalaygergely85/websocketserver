package com.gege.ideas.websocketserver.websocket.actions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gege.ideas.websocketserver.message.constans.MessageConstans;
import com.gege.ideas.websocketserver.message.service.MessageStatusService;
import com.gege.ideas.websocketserver.message.service.MessageStatusType;
import com.gege.ideas.websocketserver.util.JsonUtil;
import com.gege.ideas.websocketserver.websocket.SessionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MessageStatusAction extends ActionService {
    public MessageStatusAction(WebSocketSession session, MessageStatusService messageStatusService, SessionRegistry sessionRegistry) throws IOException {
        super(session,sessionRegistry);

        this.messageStatusService = messageStatusService;
    }

    @Override
    public void handleMessage(JsonNode jsonNode) throws Exception {
        String uuid = jsonNode.has("uuid") ? jsonNode.get("uuid").asText() : null;

        ObjectMapper mapper = new ObjectMapper();

        Map<Long, MessageStatusType> userStatuses = mapper.convertValue(
                jsonNode.get("userStatuses"),
                new TypeReference<Map<Long, MessageStatusType>>() {}
        );

        Map<Long, Boolean> deliveredStatuses = mapper.convertValue(
                jsonNode.get("deliveredStatuses"),
                new TypeReference<Map<Long, Boolean>>() {}
        );

        logger.info("Message from read by: " + userStatuses.toString() + ", uuid: " + uuid);


        Map<String, Object> message = new HashMap<>();
        message.put("type", MessageConstans.MESSAGE_STATUS);
        message.put("userStatuses", userStatuses);
        message.put("uuid", uuid);
        message.put("deliveredStatuses", deliveredStatuses);

        for (Map.Entry<Long, Boolean> entry : deliveredStatuses.entrySet()) {

            Long userId = entry.getKey();
            boolean delivered = entry.getValue();
            if(!delivered) {
                sendMessageToUser(JsonUtil.mapToJsonString(message), userId);
            }

        }

        messageStatusService.markMessageAsRead(uuid, authToken);
    }


    private final MessageStatusService messageStatusService;


}
