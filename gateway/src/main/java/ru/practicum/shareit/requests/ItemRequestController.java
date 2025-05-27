package ru.practicum.shareit.requests;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.NewItemRequest;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final String userIdHeader = "X-Sharer-User-Id";
    private final ItemRequestClient itemRequestClient;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests() {
        return itemRequestClient.getAllRequests();
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader(userIdHeader) Long userId) {
        return itemRequestClient.getUserRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable Long requestId) {
        return itemRequestClient.getRequestById(requestId);
    }

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader(userIdHeader) Long userId, @Valid @RequestBody NewItemRequest newItemRequest) {
        return itemRequestClient.createItemRequest(newItemRequest, userId);
    }

}
