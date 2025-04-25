package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundItemException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> getUserItems(long userId) {
        userRepository.findUserById(userId).orElseThrow(() -> new NotFoundUserException(userId));
        return itemRepository.getUserItems(userId).stream().map(ItemMapper::mapToItemDto).toList();
    }

    @Override
    public List<ItemDto> getItemsByQuery(String query) {
        if (query.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.findItemsByQuery(query);
    }

    @Override
    public ItemDto getItemById(long itemId) {
        return itemRepository.findItemById(itemId).orElseThrow(() -> new NotFoundItemException(itemId));
    }

    @Override
    public ItemDto createItem(long userId, NewItemRequest newItemRequest) {
        userRepository.findUserById(userId).orElseThrow(() -> new NotFoundUserException(userId));
        return itemRepository.createItem(newItemRequest, userId);
    }

    @Override
    public ItemDto updateItem(long userId, UpdateItemRequest updateItemRequest, long itemId) {
        userRepository.findUserById(userId).orElseThrow(() -> new NotFoundUserException(userId));
        itemRepository.findItemById(itemId).orElseThrow(() -> new NotFoundItemException(itemId));
        return itemRepository.updateItem(userId, updateItemRequest, itemId);
    }


}
