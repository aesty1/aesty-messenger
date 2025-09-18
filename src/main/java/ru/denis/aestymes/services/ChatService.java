package ru.denis.aestymes.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.denis.aestymes.dtos.ChatDTO;
import ru.denis.aestymes.dtos.ChatRequest;
import ru.denis.aestymes.models.Chat;
import ru.denis.aestymes.models.ChatMember;
import ru.denis.aestymes.models.MyUser;
import ru.denis.aestymes.repositories.ChatMemberRepository;
import ru.denis.aestymes.repositories.ChatRepository;
import ru.denis.aestymes.repositories.MyUserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private ChatMemberRepository chatMemberRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public List<Chat> getUserChats(Long userId) {

        return chatRepository.findChatsByUserId(userId);
    }

    public Chat getChatById(Long chatId) {
        return chatRepository.findChatById(chatId).orElse(null);
    }

    public void createChat(ChatRequest chatRequest) {
        Chat chat = new Chat();
        List<ChatMember> members = new ArrayList<>();
        ChatMember chatMember1 = new ChatMember();
        ChatMember chatMember2 = new ChatMember();
        chatRequest.setCreatedAt(LocalDateTime.now());

        chatMember1.setChat(chat);
        chatMember1.setUser(myUserRepository.findMyUserByUsername(chatRequest.getMember1Username()));

        chatMember2.setChat(chat);
        chatMember2.setUser(myUserRepository.findMyUserByUsername(chatRequest.getMember2Username()));

        members.add(chatMember1);
        members.add(chatMember2);

        chat.setName(chatRequest.getName());
        chat.setIsGroupChat(chatRequest.getIsGroupChat());
        chat.setCreatedBy(myUserRepository.findMyUserByUsername(chatRequest.getCreatedByUsername()));
        chat.setAvatarUrl(chatRequest.getAvatarUrl());
        chat.setMembers(members);

        chatRepository.save(chat);
    }

//    @Transactional
//    public void getChatsByMembers(String username1, String username2) {
//
//
//        if(Objects.equals(username2, "")) {
//            MyUser user1 = myUserRepository.findMyUserByUsername(username1);
//
//            ChatMember member1 = chatMemberRepository.findChatMembersByUser(user1).getFirst();
//            System.out.println(member1);
//            System.out.println(chatRepository.findByMembersContaining(member1));
//            messagingTemplate.convertAndSend("/topic/chats/", chatRepository.findByMembersContaining(member1));
//        }
//        else {
//            MyUser user1 = myUserRepository.findMyUserByUsername(username1);
//            MyUser user2 = myUserRepository.findMyUserByUsername(username2);
//
//            ChatMember member1 = chatMemberRepository.findChatMembersByUser(user1).getFirst();
//            ChatMember member2 = chatMemberRepository.findChatMembersByUser(user2).getFirst();
//
//            List<ChatMember> members = new ArrayList<>();
//
//            members.add(member1);
//            members.add(member2);
//
//            for (int i = 0; i < chatRepository.findChatsByExactMembers(members, 2).size(); i++) {
//                System.out.println(chatRepository.findChatsByExactMembers(members, 2).get(i));
//            }
//            messagingTemplate.convertAndSend("/topic/chats/", chatRepository.findChatsByExactMembers(members, 2));
//        }
//
//    }
}
