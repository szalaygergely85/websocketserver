package com.gege.ideas.websocketserver.message.service;

import com.gege.ideas.websocketserver.conversation.service.ConversationParticipantsService;
import com.gege.ideas.websocketserver.conversation.service.ConversationService;
import com.gege.ideas.websocketserver.message.entity.Message;
import com.gege.ideas.websocketserver.message.repository.MessageRepository;

import com.gege.ideas.websocketserver.user.service.UserService;
import com.gege.ideas.websocketserver.user.service.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

   private final MessageRepository messageRepository;
   private final UserTokenService userTokenService;
   private final UserService userService;
   private final ConversationParticipantsService conversationParticipantsService;

   private final ConversationService conversationService;


   @Autowired
   public MessageService(
      MessageRepository messageRepository,
      UserTokenService userTokenService,
      UserService userService,
      ConversationParticipantsService conversationParticipantsService,
      ConversationService conversationService
   ) {
      this.messageRepository = messageRepository;
      this.userTokenService = userTokenService;
      this.userService = userService;
      this.conversationParticipantsService = conversationParticipantsService;

      this.conversationService = conversationService;
}


   public Message createMessage(Message message) {

      Message messageExisting = messageRepository.findByUuid(message.getUuid());
              if (messageExisting!=null){
return messageExisting;
              }else {
      return messageRepository.save(message);
    }
  }




   public void deleteMessage(Message message) {
      messageRepository.delete(message);
   }


    public Message getMessageById(Long messageId) {
       return messageRepository.findByMessageId(messageId);
    }

    public Message getMessageByUuid(String uuid) {
       return messageRepository.findByUuid(uuid);
    }
}
