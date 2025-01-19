package com.gege.ideas.websocketserver.message.repository;

import com.gege.ideas.websocketserver.message.entity.MessageToSend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageToSendRepository extends JpaRepository<MessageToSend, Long> {
    MessageToSend findByMessageIdAndUserId(Long messageId, Long userId);
    List<MessageToSend> findByUserIdAndDeliveredFalse(Long userId);
}
