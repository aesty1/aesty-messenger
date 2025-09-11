package ru.denis.aestymes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.denis.aestymes.models.Chat;
import ru.denis.aestymes.models.Message;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT DISTINCT c FROM Chat c " +
            "JOIN c.members m " +
            "WHERE m.user.id = :userId " +
            "AND m.isBanned = false " +
            "ORDER BY c.updatedAt DESC")
    List<Chat> findChatsByUserId(@Param("userId") Long userId);

    Optional<Chat> findChatById(Long id);

//    List<Message> findByIdOrderByCreatedAtAsc(Long chatId);
}
