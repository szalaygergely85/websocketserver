package com.gege.ideas.websocketserver.message.repository;

import com.gege.ideas.websocketserver.message.entity.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
   Message findByUuid(String uuid);
   List<Message> findByConversationIdOrderByTimestampAsc(long id);

   Message findTopByConversationIdOrderByTimestampDesc(long id);

   List<Message> findByConversationId(long id);

   Message findByMessageId(long id);
}
