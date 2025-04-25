package com.gege.ideas.websocketserver.message.entity;

import java.io.Serializable;

public class PendingMessage implements Serializable {

   private Long pendingMessageId;

   private String uuid;

   private Long userId;

   private boolean delivered;

   public PendingMessage(String uuid, Long userId, boolean delivered) {
      this.uuid = uuid;
      this.userId = userId;
      this.delivered = delivered;
   }

   public PendingMessage() {}

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

   public boolean isDelivered() {
      return delivered;
   }

   public void setDelivered(boolean delivered) {
      this.delivered = delivered;
   }
}
