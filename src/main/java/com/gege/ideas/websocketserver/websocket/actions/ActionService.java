package com.gege.ideas.websocketserver.websocket.actions;

import com.fasterxml.jackson.databind.JsonNode;
import com.gege.ideas.websocketserver.websocket.SessionRegistry;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ActionService {
    protected final WebSocketSession session;

    public ActionService(WebSocketSession session, SessionRegistry sessionRegistry) {
        this.session = session;
        this.sessionRegistry = sessionRegistry;
    }

    public abstract void handleMessage(JsonNode jsonNode) throws Exception;

    protected void sendMessageBack(String message) throws IOException {
        if (session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        }
    }
    protected void sendMessageToUsers(String message, List<Long> userIds) throws IOException {
        for (long userId : userIds) {
            sendMessageToUser(message, userId);
        }
    }

    protected void sendMessageToUser(String message, long userId) throws IOException {
            WebSocketSession sessionTo = sessionRegistry.getSession(
                    String.valueOf(userId)
            );
            if (sessionTo != null && sessionTo.isOpen()) {
                System.out.println("Forwarding:" + message);
                sessionTo.sendMessage(new TextMessage(message));
            }

    }


    private final SessionRegistry sessionRegistry;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
}
