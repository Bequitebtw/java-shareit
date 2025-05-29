package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.NewCommentRequest;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;
    private final String userIdHeader = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader(userIdHeader) long userId) {
        return itemClient.getUserItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsByQuery(@RequestParam String text) {
        return itemClient.getItemsByQuery(text);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable long itemId) {
        return itemClient.getItemById(itemId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(userIdHeader) long userId,
                                             @RequestBody @Valid NewItemRequest newItemRequest) {
        return itemClient.createItem(userId, newItemRequest);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(userIdHeader) long userId,
                                             @RequestBody @Valid UpdateItemRequest updateItemRequest,
                                             @PathVariable long itemId) {
        return itemClient.updateItem(userId, updateItemRequest, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(userIdHeader) long userId, @PathVariable long itemId,
                                                @RequestBody @Valid NewCommentRequest comment) {
        return itemClient.createComment(itemId, userId, comment);
    }
}

