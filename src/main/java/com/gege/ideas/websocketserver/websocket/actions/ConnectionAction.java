package com.gege.ideas.websocketserver.websocket.actions;

import com.gege.ideas.websocketserver.user.service.UserService;
import com.gege.ideas.websocketserver.websocket.MyWebSocketHandler;
import com.gege.ideas.websocketserver.websocket.SessionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
@Component
public class ConnectionAction {

    private static final Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);
    private UserService userService;
    @Autowired
    public ConnectionAction(UserService userService) {
        this.userService = userService;
    }

    public Long registerUser(SessionRegistry sessionRegistry, WebSocketSession session) throws IOException {
        Long userId = _getUserIdFromSession(session);
    if (userId != null) {
      sessionRegistry.registerSession(userId.toString(), session);
      logger.info("User connected: " + userId);
      return userId;
        }else {
        logger.error("User not found: " + userId);
        return null;
    }
    }

    private Long _getUserIdFromSession(WebSocketSession session) throws IOException {
        session.getAttributes();
        HttpHeaders headers = session.getHandshakeHeaders();
        String token = null;
        if (headers.containsKey("token") && headers.get("token") != null) {
            token = headers.getFirst("token");
        }

        if (token == null) {
            // Handle the case where the token is missing
            logger.error("Token is missing in the headers.");
            session.close();
            return null;
        }

        return userService.getUserIdByToken(token);
    }
}
