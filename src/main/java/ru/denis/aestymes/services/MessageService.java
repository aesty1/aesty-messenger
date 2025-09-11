package ru.denis.aestymes.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.denis.aestymes.dtos.ChatDTO;
import ru.denis.aestymes.dtos.ChatMemberDTO;
import ru.denis.aestymes.dtos.MessageDto;
import ru.denis.aestymes.dtos.UserDTO;
import ru.denis.aestymes.models.Chat;
import ru.denis.aestymes.models.ChatMember;
import ru.denis.aestymes.models.Message;
import ru.denis.aestymes.models.MyUser;
import ru.denis.aestymes.repositories.ChatRepository;
import ru.denis.aestymes.repositories.MessageRepository;
import ru.denis.aestymes.repositories.MyUserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MyUserRepository myUserRepository;

    public List<Message> getMessagesByChat(Chat chat) {
        return messageRepository.findByChatOrderByCreatedAtAsc(chat);
    }

    @Transactional
    public void sendMessagesByChatId(Long chatId) {
        Chat chat = chatService.getChatById(chatId);
        List<Message> messages = messageRepository.findByChatOrderByCreatedAtAsc(chat);

        List<MessageDto> messageDTOs = messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        messagingTemplate.convertAndSend("/topic/chat/" + chatId, messageDTOs);
    }

    @Transactional
    public void sendMessage(Long chatId, Long senderId, String content) {
        Chat chat = chatRepository.findChatById(chatId).orElseThrow(() -> new EntityNotFoundException("Chat not found"));
        MyUser sender = myUserRepository.findById(senderId).orElseThrow(() -> new EntityNotFoundException("Sender not found"));

        Message message = new Message();

        message.setChat(chat);
        message.setSender(sender);
        message.setContent(content);


        messageRepository.save(message);

        MessageDto messageDto = convertToDTO(message);

        messagingTemplate.convertAndSend("/topic/chat/message/" + chatId, messageDto);
    }

    private MessageDto convertToDTO(Message message) {
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setCreatedAt(message.getCreatedAt());
        dto.setSenderNickname(message.getSender().getName());
        dto.setSenderId(message.getSender().getId());

        if (message.getChat() != null) {
            dto.setChat(convertChatToDTO(message.getChat()));
        }

        return dto;
    }

    private ChatDTO convertChatToDTO(Chat chat) {
        ChatDTO dto = new ChatDTO();
        dto.setId(chat.getId());
        dto.setName(chat.getName());

        if (chat.getCreatedBy() != null) {
            dto.setCreatedBy(convertUserToDTO(chat.getCreatedBy()));
        }

        // Конвертируем members без обратных ссылок
        if (chat.getMembers() != null) {
            dto.setMembers(chat.getMembers().stream()
                    .map(this::convertChatMemberToDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }


    private ChatMemberDTO convertChatMemberToDTO(ChatMember chatMember) {
        ChatMemberDTO dto = new ChatMemberDTO();
        dto.setId(chatMember.getId());

        if (chatMember.getUser() != null) {
            dto.setUser(convertUserToDTO(chatMember.getUser()));
        }

        // НЕ включаем обратную ссылку на chat!
        return dto;
    }

    private UserDTO convertUserToDTO(MyUser user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        return dto;
    }
}

