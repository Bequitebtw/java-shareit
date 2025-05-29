package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemRequestDto {
    private long id;
    private String description;
    private User requester;
    private LocalDateTime created = LocalDateTime.now();
}
