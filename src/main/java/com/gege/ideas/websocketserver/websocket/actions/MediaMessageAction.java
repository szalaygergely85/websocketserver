package com.gege.ideas.websocketserver.websocket.actions;

import com.fasterxml.jackson.databind.JsonNode;
import com.gege.ideas.websocketserver.DTO.ConversationDTO;
import com.gege.ideas.websocketserver.conversation.entity.Conversation;
import com.gege.ideas.websocketserver.conversation.entity.ConversationParticipant;
import com.gege.ideas.websocketserver.conversation.service.ConversationService;
import com.gege.ideas.websocketserver.message.entity.Message;
import com.gege.ideas.websocketserver.message.service.MessageService;
import com.gege.ideas.websocketserver.util.JsonUtil;
import com.gege.ideas.websocketserver.websocket.SessionRegistry;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public class MediaMessageAction extends ActionService {

    private final MessageService messageService;
    private final ConversationService conversationService;

    public MediaMessageAction(WebSocketSession session,ConversationService conversationService, MessageService messageService, String authToken, SessionRegistry sessionRegistry) {
        super(session, sessionRegistry);
        this.messageService = messageService;
        this.authToken = authToken;
        this.conversationService = conversationService;
    }

    @Override
    public void handleMessage(JsonNode jsonNode) throws Exception {
        Message messageLocal = messageService.addMessage(
                JsonUtil.jsonToMessage(jsonNode),
                authToken
        );


        ConversationDTO conversationDTO =
                conversationService.getConversation(
                        messageLocal.getConversationId(),
                        authToken
                );

        List<ConversationParticipant> conversationParticipants =
                conversationDTO.getParticipants();


        for (ConversationParticipant conversationParticipant : conversationParticipants) {
            if (
                    conversationParticipant.getUserId() !=
                            messageLocal.getSenderId()
            ) {
               sendMessageToUser(JsonUtil.jsonNodeToString(jsonNode), conversationParticipant.getUserId());
            }
        }
    }

    private final String authToken;
}
