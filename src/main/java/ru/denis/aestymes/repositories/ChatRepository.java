package ru.denis.aestymes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.denis.aestymes.models.Chat;
import ru.denis.aestymes.models.ChatMember;
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

    @Query("SELECT c FROM Chat c WHERE " +
            "(SELECT COUNT(cm) FROM c.members cm WHERE cm IN :members) = :memberCount " +
            "AND SIZE(c.members) = :memberCount")
    List<Chat> findChatsByExactMembers(@Param("members") List<ChatMember> members,
                                       @Param("memberCount") long memberCount);
    List<Chat> findByMembersContaining(ChatMember member);
    Optional<Chat> findChatById(Long id);
    Boolean existsByMembersContains(List<ChatMember> members);

    List<Chat> findByName(String name);

//    List<Message> findByIdOrderByCreatedAtAsc(Long chatId);
}
