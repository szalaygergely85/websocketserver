package com.gege.ideas.websocketserver.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long userId;

   @Column(nullable = false)
   private String displayName;

   @Column(nullable = false)
   private String email;

   @Column(nullable = false)
   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
   private String password;

   @Column(nullable = false)
   private Long userTokenId;

   @Lob
   @Column(columnDefinition = "LONGTEXT")
   private String publicKey;

   @Column
   String profilePictureUuid;

   @Column
   String status;

   @Column
   String tags;

   public User() {}

   public Long getUserId() {
      return userId;
   }

   public void setUserId(Long userId) {
      this.userId = userId;
   }

   public Long getUserTokenId() {
      return userTokenId;
   }

   public void setUserTokenId(Long userTokenId) {
      this.userTokenId = userTokenId;
   }

   public String getPublicKey() {
      return publicKey;
   }

   public void setPublicKey(String publicKey) {
      this.publicKey = publicKey;
   }
}
