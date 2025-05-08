package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.List;

public interface ItemService {
    ItemDto createItem(long userId, NewItemRequest newItemRequest);

    ItemDto getItemById(long itemId);

    ItemDto updateItem(long userId, UpdateItemRequest updateItemRequest, long itemId);

    List<ItemDto> getUserItems(long userId);

    List<ItemDto> getItemsByQuery(String query);

    Comment createComment(long itemId,long userId,Comment comment);
}
