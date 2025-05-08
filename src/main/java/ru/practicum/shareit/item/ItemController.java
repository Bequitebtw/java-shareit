package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final String userIdHeader = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader(userIdHeader) long userId) {
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByQuery(@RequestParam String text) {
        return itemService.getItemsByQuery(text);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        return itemService.getItemById(itemId);
    }

    @PostMapping()
    public ItemDto createItem(@RequestHeader(userIdHeader) long userId,
                              @RequestBody @Valid NewItemRequest newItemRequest) {
        return itemService.createItem(userId, newItemRequest);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(userIdHeader) long userId,
                              @RequestBody @Valid UpdateItemRequest updateItemRequest,
                              @PathVariable long itemId) {
        return itemService.updateItem(userId, updateItemRequest, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public Comment createComment(@RequestHeader(userIdHeader) long userId,@PathVariable long itemId,
                              @RequestBody @Valid Comment comment) {
        return itemService.createComment(itemId, userId, comment);
    }
}
