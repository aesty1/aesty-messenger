package ru.denis.aestymes.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDTO {
    private Long id;
    private String name;
    private UserDTO createdBy;
    private List<ChatMemberDTO> members;

    // конструкторы, геттеры, сеттеры
}
