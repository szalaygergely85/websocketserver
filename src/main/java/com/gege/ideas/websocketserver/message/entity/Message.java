package com.gege.ideas.websocketserver.message.entity;

import java.io.Serializable;
import javax.persistence.*;

public class Message implements Serializable {

   private Long messageId;

   private long conversationId;

   private long senderId;

   private long timestamp;

   private String contentEncrypted;

   private int type;

   private String uuid;

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
      return contentEncrypted;
   }

   public void setContentEncrypted(String content) {
      this.contentEncrypted = content;
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
      String contentEncrypted,
      int type,
      String uuid
   ) {
      this.conversationId = conversationId;
      this.senderId = senderId;
      this.timestamp = timestamp;
      this.contentEncrypted = contentEncrypted;

      this.type = type;

      this.uuid = uuid;
   }
}
