package ru.denis.aestymes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.denis.aestymes.models.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
}
