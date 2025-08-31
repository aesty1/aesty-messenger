package ru.denis.aestymes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.denis.aestymes.models.ChatMember;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
}
