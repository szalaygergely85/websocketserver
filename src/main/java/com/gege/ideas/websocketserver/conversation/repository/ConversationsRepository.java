package com.gege.ideas.websocketserver.conversation.repository;


import com.gege.ideas.websocketserver.conversation.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationsRepository
   extends JpaRepository<Conversation, Long> {
   Conversation findConversationByConversationId(Long conversationId);
   void deleteConversationByConversationId(Long conversationId);
}
