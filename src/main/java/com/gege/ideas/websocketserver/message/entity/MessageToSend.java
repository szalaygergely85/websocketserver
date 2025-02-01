package com.gege.ideas.websocketserver.message.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "messagetosend")
public class MessageToSend implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long messageToSendId;

   @Column(nullable = false)
   private Long messageId;

   @Column(nullable = false)
   private Long userId;

   @Column
   private boolean delivered;

   public MessageToSend(Long messageId, Long userId) {
      this.messageId = messageId;
      this.userId = userId;
      this.delivered = false;
   }

   public MessageToSend() {}

   public Long getMessageId() {
      return messageId;
   }

   public void setMessageId(Long messageId) {
      this.messageId = messageId;
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
