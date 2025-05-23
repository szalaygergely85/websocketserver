package com.gege.ideas.websocketserver.config;

import com.gege.ideas.websocketserver.websocket.MyWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

   @Override
   public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
      registry.addHandler(myWebSocketHandler(), "/ws").setAllowedOrigins("*");
   }

   @Bean
   public MyWebSocketHandler myWebSocketHandler() {
      return new MyWebSocketHandler();
   }
}
