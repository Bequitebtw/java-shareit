package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundRequestException;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.request.dto.NewItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemRequest createItemRequest(NewItemRequest newItemRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException(userId));
        return itemRequestRepository.save(ItemRequestMapper.newItemRequestToItemRequest(newItemRequest, user));
    }

    public List<ItemRequest> getUserRequests(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException(userId));
        return itemRequestRepository.findItemRequestsByRequesterId(userId);
    }


    @Override
    public List<ItemRequest> getAllRequests() {
        return itemRequestRepository.findAllByOrderByCreatedDesc();
    }

    @Override
    public ItemRequest getRequestById(Long requestId) {
        return itemRequestRepository.findById(requestId).orElseThrow(() -> new NotFoundRequestException(requestId));
    }
}
