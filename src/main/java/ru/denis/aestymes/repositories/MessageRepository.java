package ru.denis.aestymes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.denis.aestymes.models.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
