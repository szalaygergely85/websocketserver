package com.gege.ideas.websocketserver.websocket.actions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.gege.ideas.websocketserver.user.service.UserService;
import com.gege.ideas.websocketserver.websocket.SessionRegistry;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.WebSocketSession;

@ExtendWith(MockitoExtension.class)
public class ConnectionActionTest {

   @Mock
   private SessionRegistry sessionRegistry;

   @Mock
   private UserService userService;

   @Mock
   private WebSocketSession session;

   @InjectMocks
   private ConnectionAction connectionAction;

   private HttpHeaders headers;

   @BeforeEach
   void setUp() {
      headers = new HttpHeaders();
   }

   @Test
   void testRegisterUser_ValidUser() throws IOException {
      headers.add("token", "validToken");
      when(session.getHandshakeHeaders()).thenReturn(headers);
      when(userService.getUserIdByToken("validToken")).thenReturn(123L);
      when(session.getLocalAddress())
         .thenReturn(new InetSocketAddress("localhost", 8080));

      Long userId = connectionAction.registerUser(sessionRegistry, session);

      assertNotNull(userId);
      assertEquals(123L, userId);
      verify(sessionRegistry).registerSession("123", session);
   }

   @Test
   void testRegisterUser_MissingToken() throws IOException {
      when(session.getHandshakeHeaders()).thenReturn(headers);
      doNothing().when(session).close();

      Long userId = connectionAction.registerUser(sessionRegistry, session);

      assertNull(userId);
      verify(session).close();
      verifyNoInteractions(sessionRegistry);
   }

   @Test
   void testRegisterUser_InvalidToken() throws IOException {
      headers.add("token", "invalidToken");
      when(session.getHandshakeHeaders()).thenReturn(headers);
      when(userService.getUserIdByToken("invalidToken")).thenReturn(null);
      doNothing().when(session).close();

      Long userId = connectionAction.registerUser(sessionRegistry, session);

      assertNull(userId);
      verify(session).close();
      verifyNoInteractions(sessionRegistry);
   }
}
