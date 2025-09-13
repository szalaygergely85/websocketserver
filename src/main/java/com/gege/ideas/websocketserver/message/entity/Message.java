package com.gege.ideas.websocketserver.message.entity;

import java.io.Serializable;

public class Message implements Serializable {

   private Long messageId;

   private long conversationId;

   private long senderId;

   private long timestamp;

   private String content;

   private boolean encrypted;

   private int type;

   private String uuid;

   private Long conversationOrderId;

   public void setEncrypted(boolean encrypted) {
      this.encrypted = encrypted;
   }

   public Long getConversationOrderId() {
      return conversationOrderId;
   }

   public void setConversationOrderId(Long conversationOrderId) {
      this.conversationOrderId = conversationOrderId;
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

   public String getContent() {
      return content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public boolean isEncrypted() {
      return encrypted;
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
      boolean encrypted,
      int type,
      String uuid,
      Long conversationOrderId
   ) {
      this.conversationId = conversationId;
      this.senderId = senderId;
      this.timestamp = timestamp;
      this.content = content;
      this.encrypted = encrypted;
      this.type = type;

      this.uuid = uuid;
      this.conversationOrderId = conversationOrderId;
   }
}
