package ru.denis.aestymes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.denis.aestymes.models.ChatMember;
import ru.denis.aestymes.models.MyUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    ChatMember findChatMemberByUser(MyUser user);
}
