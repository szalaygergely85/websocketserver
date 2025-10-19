package com.gege.ideas.websocketserver.websocket.actions;

import com.fasterxml.jackson.databind.JsonNode;
import com.gege.ideas.websocketserver.message.constans.MessageConstans;
import com.gege.ideas.websocketserver.message.entity.Message;
import com.gege.ideas.websocketserver.message.entity.MessageStatus;
import com.gege.ideas.websocketserver.message.service.MessageService;
import com.gege.ideas.websocketserver.message.service.MessageStatusService;
import com.gege.ideas.websocketserver.user.service.UserService;
import com.gege.ideas.websocketserver.websocket.SessionRegistry;
import java.io.IOException;
import java.util.List;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class ConnectionAction extends ActionService {

   private UserService userService;
   private MessageService messageService;
   private MessageStatusService messageStatusService;

   public ConnectionAction(
      WebSocketSession session,
      SessionRegistry sessionRegistry,
      UserService userService,
      MessageService messageService,
      MessageStatusService messageStatusService
   ) throws IOException {
      super(session, sessionRegistry);
      this.userService = userService;
      this.messageService = messageService;
      this.messageStatusService = messageStatusService;
   }

   @Override
   public void handleMessage(JsonNode jsonNode) throws Exception {}

   public Long registerUser() throws IOException {
      Long userId = _getUserIdFromSession();
      if (userId != null) {
         sessionRegistry.registerSession(userId.toString(), session);
         logger.info("User {} is connected ", userId);
         _getNotDeliveredMessages(userId);
         return userId;
      } else {
         String errorMessage =
            "{\"type\":" +
            MessageConstans.ERROR +
            ",\"error_type\":" +
            MessageConstans.ERROR_USER_NOT_FOUND +
            "}";
         session.sendMessage(new TextMessage(errorMessage));
         session.close();
         return null;
      }
   }

   private void _getNotDeliveredMessages(Long userId) throws IOException {
      if (userId != null) {
         List<Message> messageList = messageService.getNotDeliveredMessages(
            authToken
         );
         logger.info(
            userId + " not delivered Message count: " + messageList.size()
         );
         sendMessagesToSession(messageList, session);
         List<MessageStatus> messageStatusList =
            messageStatusService.getNotDeliveredMessages(authToken);
         logger.info(
            userId +
            " not delivered Message Status count: " +
            messageStatusList.size()
         );
         sendMessagesStatusesToSession(messageStatusList, session);
      } else {
         session.close();
      }
   }

   private Long _getUserIdFromSession() throws IOException {
      String token = getAuthToken();

      return userService.getUserIdByToken(token);
   }
}
