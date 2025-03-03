package com.gege.ideas.websocketserver.websocket.actions;

import static com.gege.ideas.websocketserver.util.JsonUtil.objectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.gege.ideas.websocketserver.message.constans.MessageConstans;
import com.gege.ideas.websocketserver.message.entity.Message;
import com.gege.ideas.websocketserver.message.entity.MessageToSend;
import com.gege.ideas.websocketserver.message.service.MessageService;
import com.gege.ideas.websocketserver.message.service.MessageToSendService;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class MessageActionTest {

   @Mock
   private MessageToSendService messageToSendService;

   @Mock
   private MessageService messageService;

   @Mock
   private WebSocketSession session;

   @InjectMocks
   private MessageAction messageAction;

   @BeforeEach
   void setUp() {
      MockitoAnnotations.openMocks(this); // Initializes mocks
   }

   @Test
   void testGetNotDeliveredMessages() {
      // Mock dependencies
      MessageToSend msgToSend = new MessageToSend();
      msgToSend.setMessageId(1L);
      Message message = new Message(
         1L,
         2L,
         123456789L,
         "Encrypted Content",
         MessageConstans.MESSAGE,
         "uuid-123"
      );

      when(messageToSendService.getNotDeliveredMessages(1L))
         .thenReturn(List.of(msgToSend));
      when(messageService.getMessageById(1L)).thenReturn(message);

      // Call method
      List<Message> messages = messageAction.getNotDeliveredMessages(1L);

      // Verify results
      assertEquals(1, messages.size());
      assertEquals("uuid-123", messages.get(0).getUuid());
      verify(messageToSendService).getNotDeliveredMessages(1L);
      verify(messageService).getMessageById(1L);
   }

   @Test
   void testAnsweringToPing() throws IOException {
      when(session.isOpen()).thenReturn(true);

      messageAction.answeringToPing(session);

      verify(session).sendMessage(new TextMessage("{\"type\": \"pong\"}"));
   }

   @Test
   void testSaveJsonToMessage_ValidInput() throws Exception {
      String json =
         """
         	{
         		\"senderId\": \"1\",
         		\"conversationId\": \"2\",
         		\"uuid\": \"abc-123\",
         		\"timestamp\": \"1700000000\",
         		\"contentEncrypted\": \"encryptedContent\"
         	}
         """;
      JsonNode jsonNode = objectMapper.readTree(json);
      Message expectedMessage = new Message(
         2L,
         1L,
         1700000000L,
         "encryptedContent",
         MessageConstans.MESSAGE,
         "abc-123"
      );
      when(messageService.createMessage(any(Message.class)))
         .thenReturn(expectedMessage);

      Message result = messageAction.saveJsonToMessage(jsonNode);

      assertNotNull(result);
      assertEquals(2L, result.getConversationId());
      assertEquals(1L, result.getSenderId());
      assertEquals(1700000000L, result.getTimestamp());
      assertEquals("encryptedContent", result.getContentEncrypted());
      assertEquals("abc-123", result.getUuid());
      assertEquals(MessageConstans.MESSAGE, result.getType());
      verify(messageService).createMessage(any(Message.class));
   }

   @Test
   void testSendMessages() throws IOException {
      Message message = new Message(
         1L,
         2L,
         123456789L,
         "Encrypted Content",
         MessageConstans.MESSAGE,
         "uuid-123"
      );
      List<Message> messageList = Arrays.asList(message);

      when(session.isOpen()).thenReturn(true);

      messageAction.sendMessages(messageList, session);

      verify(session).sendMessage(any(TextMessage.class));
   }
}
