package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundItemException;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.user.JpaUserRepository;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final JpaItemRepository itemRepository;
    private final JpaUserRepository userRepository;

    @Override
    public List<ItemDto> getUserItems(long userId) {
        return itemRepository.findByUserId(userId)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemsByQuery(String query) {
        if(query.isEmpty()){
            return List.of();
        }
        return itemRepository.findByAvailableTrueAndNameContainingIgnoreCaseOrAvailableTrueAndDescriptionContainingIgnoreCase
                        (query,query)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(()-> new NotFoundItemException(itemId));
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto createItem(long userId, NewItemRequest newItemRequest) {
        User user = userRepository.findById(userId).orElseThrow(()-> new NotFoundUserException(userId));
        Item item = ItemMapper.mapToItem(newItemRequest);
        item.setUser(user);
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(long userId, UpdateItemRequest updateItemRequest, long itemId) {
        userRepository.findById(userId).orElseThrow(()-> new NotFoundUserException(userId));
        Item item = itemRepository.findById(itemId).orElseThrow(()-> new NotFoundItemException(itemId));
        return ItemMapper.mapToItemDto(itemRepository.save(ItemMapper.updateItemFields(updateItemRequest,item)));
    }


}
