package ru.practicum.shareit.requests.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.dto.NewUserRequest;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemRequestDto {
    private long id;
    private String description;
    private NewUserRequest requester;
    private LocalDateTime created = LocalDateTime.now();
}
