package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.NewItemRequest;
import ru.practicum.shareit.user.User;

public class ItemRequestMapper {

    public static ItemRequest newItemRequestToItemRequest(NewItemRequest newItemRequest, User user) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(newItemRequest.getDescription());
        itemRequest.setRequester(user);
        return itemRequest;
    }
}
