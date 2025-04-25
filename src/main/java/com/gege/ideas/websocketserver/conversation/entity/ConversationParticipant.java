package com.gege.ideas.websocketserver.conversation.entity;

public class ConversationParticipant {

   private Long conversationParticipantId;

   private Long conversationId;

   private Long userId;

   public Long getConversationParticipantId() {
      return conversationParticipantId;
   }

   public void setConversationParticipantId(Long conversationParticipantId) {
      this.conversationParticipantId = conversationParticipantId;
   }

   public Long getConversationId() {
      return conversationId;
   }

   public void setConversationId(Long conversationId) {
      this.conversationId = conversationId;
   }

   public Long getUserId() {
      return userId;
   }

   public void setUserId(Long userId) {
      this.userId = userId;
   }

   public ConversationParticipant(Long conversationId, Long userId) {
      this.conversationId = conversationId;
      this.userId = userId;
   }

   public ConversationParticipant() {}
}
