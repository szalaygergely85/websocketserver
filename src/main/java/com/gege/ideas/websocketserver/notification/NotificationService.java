package com.gege.ideas.websocketserver.notification;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

   private final NotificationApiClient notificationApiClient;

   @Autowired
   public NotificationService(NotificationApiClient notificationApiClient) {
      this.notificationApiClient = notificationApiClient;
   }

   public void sendNotification(
      String content,
      String type,
      long userId,
      String title
   ) {
      Map<String, String> data = new HashMap<>();

      data.put("content", content);
      data.put("title", title);

      notificationApiClient.sendNotification(data, type, userId);
   }
}
