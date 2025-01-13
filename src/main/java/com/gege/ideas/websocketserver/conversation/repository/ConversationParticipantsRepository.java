package com.gege.ideas.websocketserver.conversation.repository;


import com.gege.ideas.websocketserver.conversation.entity.ConversationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationParticipantsRepository
   extends JpaRepository<ConversationParticipant, Long> {
   List<ConversationParticipant> findByUserId(Long userId);

   List<ConversationParticipant> findByConversationId(Long id);

   void deleteConversationParticipantsByConversationId(Long conversationId);
}
