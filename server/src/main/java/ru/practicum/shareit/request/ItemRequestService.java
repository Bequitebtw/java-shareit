package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.NewItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest createItemRequest(NewItemRequest newItemRequest, Long userId);

    List<ItemRequest> getUserRequests(Long userId);

    List<ItemRequest> getAllRequests();

    ItemRequest getRequestById(Long requestId);
}
