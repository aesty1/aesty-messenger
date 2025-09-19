package ru.denis.aestymes.repositories;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.denis.aestymes.models.Chat;
import ru.denis.aestymes.models.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatOrderByCreatedAtAsc(Chat chat);
    Message findByContentContaining(String content);

    List<Message> findByContentContainingIgnoreCaseAndChat(String content, Chat chat);

}
