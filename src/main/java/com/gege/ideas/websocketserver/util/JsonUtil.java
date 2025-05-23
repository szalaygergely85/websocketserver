package com.gege.ideas.websocketserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

   public static final ObjectMapper objectMapper = new ObjectMapper();

   public static String toJson(Object object) {
      try {
         return objectMapper.writeValueAsString(object);
      } catch (JsonProcessingException e) {
         e.printStackTrace();
         return null;
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
}
