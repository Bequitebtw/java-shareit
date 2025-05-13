package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NoAccessToItemException;
import ru.practicum.shareit.exception.NotFoundItemException;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public List<ItemDto> getUserItems(long userId) {
        return itemRepository.findByUserId(userId)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemsByQuery(String query) {
        if (query.isEmpty()) {
            return List.of();
        }
        return itemRepository.searchAvailableItemsByText(query)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundItemException(itemId));
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto createItem(long userId, NewItemRequest newItemRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException(userId));
        Item item = ItemMapper.mapToItem(newItemRequest);
        item.setUser(user);
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }


    @Override
    public ItemDto updateItem(long userId, UpdateItemRequest updateItemRequest, long itemId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException(userId));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundItemException(itemId));
        return ItemMapper.mapToItemDto(itemRepository.save(ItemMapper.updateItemFields(updateItemRequest, item)));
    }

    @Override
    @Transactional
    public Comment createComment(long itemId, long userId, Comment comment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(userId));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundItemException(itemId));
        if (bookingRepository.findLastBookingEndDateByItemId(itemId).isAfter(LocalDateTime.now())) {
            throw new BadRequestException("ваше бронирование еще не окончено");
        }

        boolean hasBooking = bookingRepository.existsByBookerIdAndItemIdAndStatus(userId, itemId, BookingStatus.APPROVED);
        if (!hasBooking) {
            throw new NoAccessToItemException(userId, itemId);
        }

        comment.setItem(item);
        comment.setAuthor(user);
        comment.setAuthorName(user.getName());

        return commentRepository.save(comment);
    }

}
