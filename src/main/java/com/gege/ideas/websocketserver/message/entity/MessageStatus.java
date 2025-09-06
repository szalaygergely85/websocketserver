package com.gege.ideas.websocketserver.message.entity;

import com.gege.ideas.websocketserver.message.constans.MessageConstans;
import com.gege.ideas.websocketserver.message.service.MessageStatusType;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MessageStatus implements Serializable {

   private Long messageStatusId;

   private String uuid;
   private Map<Long, MessageStatusType> userStatuses = new HashMap<>();
   private Map<Long, Boolean> deliveredStatuses = new HashMap<>();
   private final int type = MessageConstans.MESSAGE_STATUS;

   public MessageStatus(
      Long messageStatusId,
      String uuid,
      Map<Long, MessageStatusType> userStatuses,
      Map<Long, Boolean> deliveredStatuses
   ) {
      this.messageStatusId = messageStatusId;
      this.uuid = uuid;
      this.userStatuses = userStatuses;
      this.deliveredStatuses = deliveredStatuses;
   }

   public MessageStatus() {}

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

   public Map<Long, MessageStatusType> getUserStatuses() {
      return userStatuses;
   }

   public void setUserStatuses(Map<Long, MessageStatusType> userStatuses) {
      this.userStatuses = userStatuses;
   }

   public Map<Long, Boolean> getDeliveredStatuses() {
      return deliveredStatuses;
   }

   public void setDeliveredStatuses(Map<Long, Boolean> deliveredStatuses) {
      this.deliveredStatuses = deliveredStatuses;
   }

   public int getType() {
      return type;
   }
}
