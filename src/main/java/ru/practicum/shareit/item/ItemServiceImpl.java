package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.NoAccessToItemException;
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
@Transactional
@Service
public class ItemServiceImpl implements ItemService {
    private final JpaItemRepository itemRepository;
    private final JpaUserRepository userRepository;
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
        if(query.isEmpty()){
            return List.of();
        }
        return itemRepository.searchAvailableItemsByText
                        (query)
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

    @Override
    public Comment createComment(long itemId, long userId, Comment comment) {
        User user = userRepository.findById(userId).orElseThrow(()-> new NotFoundUserException(userId));
        Item item = itemRepository.findById(itemId).orElseThrow(()-> new NotFoundItemException(itemId));

//        boolean isBookedItem = bookingRepository.findBookingsByBooker(userId).stream()
//                .anyMatch(booking -> booking.getItem().equals(item) && booking.getStatus().equals(BookingStatus.APPROVED));  // Проверяем вещь

        boolean isBookedFromDB = bookingRepository.existsByBookerAndItemAndStatus(user,item,BookingStatus.APPROVED);

        if(!isBookedFromDB){
            throw new NoAccessToItemException(userId,itemId);
        }

        comment.setItem(item);
        comment.setAuthorName(user.getName());
        comment.setAuthor(user);

        commentRepository.save(comment);

        return comment;
    }

}
