package com.gege.ideas.websocketserver.message.entity;

import java.io.Serializable;

public class Message implements Serializable {

   private Long messageId;

   private long conversationId;

   private long senderId;

   private long timestamp;

   private String content;

   private boolean enrypted;

   private int type;

   private String uuid;

   public boolean getEncrypted() {
      return enrypted;
   }

   public void setEncrypted(boolean encrypted) {
      enrypted = encrypted;
   }

   public Long getMessageId() {
      return messageId;
   }

   public void setMessageId(Long messageId) {
      this.messageId = messageId;
   }

   public long getConversationId() {
      return conversationId;
   }

   public void setConversationId(long conversationId) {
      this.conversationId = conversationId;
   }

   public long getSenderId() {
      return senderId;
   }

   public void setSenderId(long senderId) {
      this.senderId = senderId;
   }

   public long getTimestamp() {
      return timestamp;
   }

   public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
   }

   public String getContentEncrypted() {
      return content;
   }

   public void setContentEncrypted(String content) {
      this.content = content;
   }

   public int getType() {
      return type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public String getUuid() {
      return uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public Message() {}

   public Message(
      long conversationId,
      long senderId,
      long timestamp,
      String content,
      boolean enrypted,
      int type,
      String uuid
   ) {
      this.conversationId = conversationId;
      this.senderId = senderId;
      this.timestamp = timestamp;
      this.content = content;
      this.enrypted = enrypted;
      this.type = type;

      this.uuid = uuid;
   }
}
