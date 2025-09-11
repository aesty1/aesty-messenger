package ru.denis.aestymes.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.denis.aestymes.jwts.JwtProvider;
import ru.denis.aestymes.models.Chat;
import ru.denis.aestymes.services.ChatService;
import ru.denis.aestymes.services.MessageService;
import ru.denis.aestymes.services.MyUserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import javax.validation.constraints.Min;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private MyUserService myUserService;

    @Autowired
    private MessageService messageService;

    @GetMapping("/chats")
    public String getChats(Model model, HttpServletRequest request)  {
        List<Chat> chats = chatService.getUserChats(myUserService.getCurrentUserId(request));
        model.addAttribute("chats", chats);

        model.addAttribute("nickname", myUserService.getUserById(myUserService.getCurrentUserId(request)));
        model.addAttribute("currentUserId", myUserService.getCurrentUserId(request));
        model.addAttribute("currentUser", myUserService.getUserById(myUserService.getCurrentUserId(request)));

        return "chat/allChats";
    }

//    @GetMapping("/chats")
//    public String getAllChats(Model model, HttpServletRequest request) {
//
//
//        return "chat/chats";
//    }

    @GetMapping("/chats/{chatId}")
    public String getChat(@PathVariable Long chatId, Model model, HttpServletRequest request) {
        Chat chat = chatService.getChatById(chatId);

        model.addAttribute("chat", chat);
        model.addAttribute("messages", messageService.getMessagesByChat(chat));
        model.addAttribute("currentUserId", myUserService.getCurrentUserId(request));
        model.addAttribute("recipId", (chat.getMembers().getFirst().getId() == myUserService.getCurrentUserId(request)) ? chat.getMembers().getLast().getUser().getId() : chat.getMembers().getFirst().getUser().getId());
        model.addAttribute("currentUser", myUserService.getUserById(myUserService.getCurrentUserId(request)));
        model.addAttribute("nickname", myUserService.getUserById(myUserService.getCurrentUserId(request)).getUsername());

        return "chat/chat";
    }

}
