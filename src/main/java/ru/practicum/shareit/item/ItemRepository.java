package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepository {
    private final HashMap<Long, Item> items = new HashMap<>();
    private final HashMap<Long, List<Item>> userItems = new HashMap<>();

    public ItemDto createItem(NewItemRequest newItemRequest, long userId) {
        newItemRequest.setId(getNextId());
        Item item = ItemMapper.mapToItem(newItemRequest);
        items.put(item.getId(), item);
        userItems.computeIfAbsent(userId, k -> new ArrayList<>()).add(item);
        return ItemMapper.mapToItemDto(item);
    }

    public Optional<ItemDto> findItemById(long itemId) {
        if (items.containsKey(itemId)) {
            return Optional.of(ItemMapper.mapToItemDto(items.get(itemId)));
        }
        return Optional.empty();
    }

    public ItemDto updateItem(long userId, UpdateItemRequest updateItemRequest, long itemId) {
        userItems.get(userId).stream()
                .filter(item -> item.getId() == itemId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("У вас нет доступа к этому предмету"));
        return ItemMapper.mapToItemDto(ItemMapper.updateItemFields(updateItemRequest, items.get(itemId)));

    }

    public List<Item> getUserItems(long userId) {
        return userItems.get(userId);
    }

    public List<ItemDto> findItemsByQuery(String query) {
        return items.values()
                .stream()
                .filter(item -> item.getName().toLowerCase().contains(query.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(query.toLowerCase()))
                .filter(Item::getAvailable)
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    private long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
