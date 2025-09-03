package com.gege.ideas.websocketserver.message.entity;

import com.gege.ideas.websocketserver.message.constans.MessageConstans;
import com.gege.ideas.websocketserver.message.service.MessageStatusType;

import java.io.Serializable;

public class MessageStatus implements Serializable {

   private Long messageStatusId;
   private String uuid;
   private Long userId;
   private MessageStatusType messageStatusType;
   private boolean delivered = false;

   private final int type = MessageConstans.MESSAGE_STATUS;

   public MessageStatus(Long messageStatusId, String uuid, Long userId, MessageStatusType messageStatusType, boolean delivered) {
      this.messageStatusId = messageStatusId;
      this.uuid = uuid;
      this.userId = userId;
      this.messageStatusType = messageStatusType;
      this.delivered = delivered;
   }

   public MessageStatus(String uuid, Long userId, MessageStatusType messageStatusType, boolean delivered) {
      this.uuid = uuid;
      this.userId = userId;
      this.messageStatusType = messageStatusType;
      this.delivered = delivered;
   }

   public Long getMessageStatusId() {
      return messageStatusId;
   }

   public void setMessageStatusId(Long messageStatusId) {
      this.messageStatusId = messageStatusId;
   }

   public String getUuid() {
      return uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public Long getUserId() {
      return userId;
   }

   public void setUserId(Long userId) {
      this.userId = userId;
   }

   public MessageStatusType getMessageStatusType() {
      return messageStatusType;
   }

   public void setMessageStatusType(MessageStatusType messageStatusType) {
      this.messageStatusType = messageStatusType;
   }

   public boolean isDelivered() {
      return delivered;
   }

   public void setDelivered(boolean delivered) {
      this.delivered = delivered;
   }

   public MessageStatus() {
   }
}
