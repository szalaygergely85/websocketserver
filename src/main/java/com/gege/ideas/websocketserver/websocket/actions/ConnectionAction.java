package com.gege.ideas.websocketserver.websocket.actions;

import com.gege.ideas.websocketserver.message.constans.MessageConstans;
import com.gege.ideas.websocketserver.user.service.UserService;
import com.gege.ideas.websocketserver.websocket.SessionRegistry;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class ConnectionAction {

   private static final Logger logger = LoggerFactory.getLogger(
      "com.websocket.connection"
   );
   private UserService userService;

   @Autowired
   public ConnectionAction(UserService userService) {
      this.userService = userService;
   }

   public Long registerUser(
      SessionRegistry sessionRegistry,
      WebSocketSession session
   ) throws IOException {
      Long userId = _getUserIdFromSession(session);
      if (userId != null) {
         sessionRegistry.registerSession(userId.toString(), session);
         logger.info(
            "User connected: " + userId + ": " + session.getLocalAddress()
         );
         return userId;
      } else {
         String errorMessage = "{\"type\": \" " + MessageConstans.ERROR + "\", \"error_type\": \" " + MessageConstans.ERROR_USER_NOT_FOUND + " \" }";
         session.sendMessage(new TextMessage(errorMessage));
         session.close();
         return null;
      }
   }

   public String getAuthToken(WebSocketSession session) throws IOException {
      session.getAttributes();
      HttpHeaders headers = session.getHandshakeHeaders();
      String token = null;
      if (headers.containsKey("token") && headers.get("token") != null) {
         token = headers.getFirst("token");
      }

      if (token == null) {

         String errorMessage = "{\"type\": \" " + MessageConstans.ERROR + "\", \"error_type\": \" " + MessageConstans.ERROR_MISSING_AUTH_TOKEN + " \" }";
         
         session.sendMessage(new TextMessage(errorMessage));
         logger.error("Token is missing in the headers.");

         return null;
      }
      return token;
   }

   private Long _getUserIdFromSession(WebSocketSession session)
      throws IOException {
      String token = getAuthToken(session);

      return userService.getUserIdByToken(token);
   }
}
