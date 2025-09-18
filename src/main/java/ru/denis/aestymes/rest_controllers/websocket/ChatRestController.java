package ru.denis.aestymes.rest_controllers.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.denis.aestymes.dtos.ChatRequest;
import ru.denis.aestymes.dtos.MessageRequest;
import ru.denis.aestymes.services.ChatService;
import ru.denis.aestymes.services.MessageService;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class ChatRestController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatService chatService;

    @MessageMapping("/send/create/chat")
    public void createChat(@Payload ChatRequest chatRequest) {
        chatService.createChat(chatRequest);
    }

    @MessageMapping("/send")
    public void sendMessagesByChatId(@Payload Long chat_id) {
        messageService.sendMessagesByChatId(chat_id);
    }

//    @MessageMapping("/send/chats")
//    public void sendChatsByUsername(@Payload MembersDto membersDto) {
//        chatService.getChatsByMembers(membersDto.getUsername1(), membersDto.getUsername2());
//    }

    @MessageMapping("/send/message")
    public void sendMessage(@Payload MessageRequest request) {
        messageService.sendMessage(
                request.getChatId(),
                request.getSenderId(),
                request.getContent()
        );
    }
}
