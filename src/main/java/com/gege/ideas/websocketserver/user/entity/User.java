package com.gege.ideas.websocketserver.user.entity;


public class User {

   private Long userId;

   private String displayName;

   private String email;
   private String password;
   private String publicKey;

   private String profilePictureUuid;

   private String status;

   private String tags;

   private String token;

   private Long lastUpdated;

   public Long getLastUpdated() {
      return lastUpdated;
   }

   public void setLastUpdated(Long lastUpdated) {
      this.lastUpdated = lastUpdated;
   }

   public String getProfilePictureUuid() {
      return profilePictureUuid;
   }

   public void setProfilePictureUuid(String profilePictureUuid) {
      this.profilePictureUuid = profilePictureUuid;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getTags() {
      return tags;
   }

   public void setTags(String tags) {
      this.tags = tags;
   }

   public Long getUserId() {
      return userId;
   }

   public void setUserId(Long id) {
      this.userId = id;
   }

   public String getDisplayName() {
      return displayName;
   }

   public void setDisplayName(String surName) {
      this.displayName = surName;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getPublicKey() {
      return publicKey;
   }

   public void setPublicKey(String publicKey) {
      this.publicKey = publicKey;
   }

   public String getToken() {
      return token;
   }

   public void setToken(String token) {
      this.token = token;
   }

   public User() {}

}
