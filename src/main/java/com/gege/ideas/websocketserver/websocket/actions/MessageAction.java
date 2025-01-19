package com.gege.ideas.websocketserver.websocket.actions;

import com.gege.ideas.websocketserver.message.entity.Message;
import com.gege.ideas.websocketserver.message.entity.MessageToSend;
import com.gege.ideas.websocketserver.message.service.MessageService;
import com.gege.ideas.websocketserver.message.service.MessageToSendService;
import com.gege.ideas.websocketserver.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MessageAction {
    private MessageToSendService messageToSendService;

    private MessageService messageService;

    @Autowired
    public MessageAction(MessageToSendService messageToSendService, MessageService messageService) {
        this.messageToSendService = messageToSendService;
        this.messageService = messageService;
    }

    public List<Message> getNotDeliveredMessages(Long userId){
        List<MessageToSend> messagesNotDelivered =  messageToSendService.getNotDeliveredMessages(userId);
        List<Message> messageList = new ArrayList<>();
        for (MessageToSend messageNotDelivered : messagesNotDelivered){
            Message message = messageService.getMessageById(messageNotDelivered.getMessageId());
            if(message!=null){
                messageList.add(message);
            }

        }
        return messageList;

    }

    public void sendMessages(List<Message> messageList, WebSocketSession session) throws IOException {
        for (Message message : messageList) {
            try {
                String messageJson = JsonUtil.toJson(message);
                session.sendMessage(new TextMessage(messageJson));
            } catch (IOException e) {
                // Log the error or handle it appropriately
                System.err.println("Error sending message: " + e.getMessage());
                throw e; // Rethrow if necessary, or handle gracefully
            }
        }
    }
}
