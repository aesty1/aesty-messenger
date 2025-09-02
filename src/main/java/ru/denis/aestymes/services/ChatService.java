package ru.denis.aestymes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.denis.aestymes.models.Chat;
import ru.denis.aestymes.models.MyUser;
import ru.denis.aestymes.repositories.ChatRepository;
import ru.denis.aestymes.repositories.MyUserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MyUserRepository myUserRepository;

    public List<Chat> getUserChats(Long userId) {

        return chatRepository.findChatsByUserId(userId);
    }

    public Chat getChatById(Long chatId) {
        return chatRepository.findChatById(chatId).orElse(null);
    }
}
