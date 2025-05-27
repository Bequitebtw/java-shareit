package ru.practicum.shareit.item.dto;

import lombok.Getter;

@Getter
public class UpdateItemRequest {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
}
