package com.gege.ideas.websocketserver.message.repository;

import com.gege.ideas.websocketserver.message.entity.MessageToSend;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageToSendRepository
   extends JpaRepository<MessageToSend, Long> {
   MessageToSend findByMessageIdAndUserId(Long messageId, Long userId);
   List<MessageToSend> findByUserIdAndDeliveredFalse(Long userId);
}
