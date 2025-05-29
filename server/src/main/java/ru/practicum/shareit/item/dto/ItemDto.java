package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    LocalDateTime lastBooking;
    LocalDateTime nextBooking;
    List<Comment> comments;
}
