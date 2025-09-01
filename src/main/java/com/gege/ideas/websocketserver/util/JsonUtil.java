package com.gege.ideas.websocketserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gege.ideas.websocketserver.message.entity.Message;

import java.util.Map;

public class JsonUtil {

   public static final ObjectMapper objectMapper = new ObjectMapper();

   public static String objectToJson(Object object) {
      try {
         return objectMapper.writeValueAsString(object);
      } catch (JsonProcessingException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static String mapToJsonString(Map<String, ?> map) {
      try {
         return objectMapper.writeValueAsString(map);
      } catch (JsonProcessingException e) {
         throw new RuntimeException("JSON serialization failed", e);
      }
   }

   public static <T> T fromJson(String json, Class<T> clazz) {
      try {
         return objectMapper.readValue(json, clazz);
      } catch (JsonProcessingException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static Message jsonToMessage(JsonNode jsonNode) {
      try {
         Long conversationId = jsonNode.hasNonNull("conversationId")
                 ? jsonNode.get("conversationId").asLong() : null;
         Long senderId = jsonNode.hasNonNull("userId")
                 ? jsonNode.get("userId").asLong() : null;
         Long timestamp = jsonNode.hasNonNull("timestamp")
                 ? jsonNode.get("timestamp").asLong() : null;
         String content = jsonNode.hasNonNull("content")
                 ? jsonNode.get("content").asText() : null;
         boolean encrypted = jsonNode.has("encrypted") && jsonNode.get("encrypted").asBoolean(false);
         int mType = jsonNode.hasNonNull("type")
                 ? jsonNode.get("type").asInt() : 0;
         String uuid = jsonNode.hasNonNull("uuid")
                 ? jsonNode.get("uuid").asText() : null;

         return new Message(conversationId, senderId, timestamp, content, encrypted, mType, uuid);
      } catch (Exception e) {
         // Optional: log the error instead of printing stack trace
         e.printStackTrace();
         return null;
      }
   }

   public static String jsonNodeToString(JsonNode jsonNode) {
      try {
         return objectMapper.writeValueAsString(jsonNode);
      } catch (JsonProcessingException e) {
         e.printStackTrace();
         return null;
      }
   }
}
