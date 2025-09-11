package ru.denis.aestymes.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.denis.aestymes.models.Chat;
import ru.denis.aestymes.models.Message;
import ru.denis.aestymes.models.MyUser;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    private Long id;
    private String content;
    private String senderNickname;
    private Long senderId;
    private LocalDateTime createdAt;
    private ChatDTO chat;
}

