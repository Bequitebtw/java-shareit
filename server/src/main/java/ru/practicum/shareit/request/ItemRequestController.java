package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.NewItemRequest;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final String userIdHeader = "X-Sharer-User-Id";
    private final ItemRequestService itemRequestService;

    @GetMapping("/all")
    public List<ItemRequest> getAllRequests() {
        return itemRequestService.getAllRequests();
    }

    @GetMapping
    public List<ItemRequest> getUserRequests(@RequestHeader(userIdHeader) Long userId) {
        return itemRequestService.getUserRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequest getRequestById(@PathVariable Long requestId) {
        return itemRequestService.getRequestById(requestId);
    }

    @PostMapping
    public ItemRequest createItemRequest(@RequestHeader(userIdHeader) Long userId, @RequestBody NewItemRequest newItemRequest) {
        return itemRequestService.createItemRequest(newItemRequest, userId);
    }


}
