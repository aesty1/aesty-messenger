package ru.denis.aestymes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.denis.aestymes.models.Chat;
import ru.denis.aestymes.models.Message;
import ru.denis.aestymes.repositories.MessageRepository;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getMessagesByChat(Chat chat) {
        return messageRepository.findByChatOrderByCreatedAtAsc(chat);
    }
}
