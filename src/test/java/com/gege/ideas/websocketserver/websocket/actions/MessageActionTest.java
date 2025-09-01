package com.gege.ideas.websocketserver.websocket.actions;



public class MessageActionTest {
/*
   @Mock
   private MessageStatusService messageStatusService;

   @Mock
   private MessageService messageService;

   @Mock
   private MessageApiClient messageApiClient;

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
      PendingMessage msgToSend = new PendingMessage();
      msgToSend.setUuid("uuid-123");
      Message message = new Message(
         1L,
         2L,
         123456789L,
         "Encrypted Content",
         MessageConstans.MESSAGE,
         "uuid-123"
      );

      when(messageStatusService.getNotDeliveredMessages("aaa"))
         .thenReturn(List.of(msgToSend));
      when(messageService.getMessageByUuid("uuid-123", "aaa"))
         .thenReturn(message);

      // Call method
      List<Message> messages = messageAction.getNotDeliveredMessages("aaa");

      // Verify results
      assertEquals(1, messages.size());
      assertEquals("uuid-123", messages.get(0).getUuid());
      verify(messageStatusService).getNotDeliveredMessages("aaa");
      verify(messageService).getMessageByUuid("uuid-123", "aaa");
   }

   @Test
   void testAnsweringToPing() throws IOException {
      when(session.isOpen()).thenReturn(true);

      messageAction.answeringToPing(session);

      verify(session).sendMessage(new TextMessage("{\"type\": \"pong\"}"));
   }

   @Test
   void testJsonToMessage_ValidInput() throws Exception {
      String json =
         """
         	{
         		\"senderId\": \"1\",
         		\"conversationId\": \"2\",
         		\"uuid\": \"abc-123\",
         		\"timestamp\": \"1700000000\",
         		\"content\": \"encryptedContent\"
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
      when(messageApiClient.addMessage(any(Message.class), "TOKEN"))
         .thenReturn(expectedMessage);

      Message result = messageService.addMessage(
         JsonUtil.jsonToMessage(jsonNode),
         "TOKEN"
      );

      assertNotNull(result);
      assertEquals(2L, result.getConversationId());
      assertEquals(1L, result.getSenderId());
      assertEquals(1700000000L, result.getTimestamp());
      assertEquals("encryptedContent", result.getContentEncrypted());
      assertEquals("abc-123", result.getUuid());
      assertEquals(MessageConstans.MESSAGE, result.getType());
      verify(messageService).addMessage(any(Message.class), "TOKEN");
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
   }*/
}
