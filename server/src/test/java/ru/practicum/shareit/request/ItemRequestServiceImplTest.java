package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.exception.NotFoundRequestException;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.request.dto.NewItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@Rollback
@SpringBootTest(
        properties = "spring.profiles.active=h2",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImplTest {
    private final ItemRequestService itemRequestService;
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    private User user1;
    private User user2;
    private ItemRequest request1;
    private ItemRequest request2;

    @BeforeEach
    void setup() {
        user1 = new User();
        user1.setName("User1");
        user1.setEmail("user1@email.com");
        userRepository.save(user1);

        user2 = new User();
        user2.setName("User2");
        user2.setEmail("user2@email.com");
        userRepository.save(user2);

        request1 = new ItemRequest();
        request1.setDescription("Нужна дрель");
        request1.setRequester(user1);
        request1.setCreated(LocalDateTime.now().minusDays(1));
        itemRequestRepository.save(request1);

        request2 = new ItemRequest();
        request2.setDescription("Нужен молоток");
        request2.setRequester(user2);
        request2.setCreated(LocalDateTime.now());
        itemRequestRepository.save(request2);
    }

    @Test
    void createItemRequest_ShouldCreateNewRequest() {
        NewItemRequest newRequest = new NewItemRequest();
        newRequest.setDescription("Новая просьба");

        ItemRequest created = itemRequestService.createItemRequest(newRequest, user1.getId());

        assertThat(created.getId(), notNullValue());
        assertThat(created.getDescription(), equalTo("Новая просьба"));
        assertThat(created.getRequester().getId(), equalTo(user1.getId()));
    }

    @Test
    void createItemRequest_ShouldThrowWhenUserNotFound() {
        NewItemRequest newRequest = new NewItemRequest();
        newRequest.setDescription("Новая просьба");

        assertThrows(NotFoundUserException.class, () ->
                itemRequestService.createItemRequest(newRequest, 999L));
    }

    @Test
    void getUserRequests_ShouldReturnUserRequests() {
        List<ItemRequest> requests = itemRequestService.getUserRequests(user1.getId());

        assertThat(requests, hasSize(1));
        assertThat(requests.get(0).getId(), equalTo(request1.getId()));
        assertThat(requests.get(0).getDescription(), equalTo("Нужна дрель"));
    }

    @Test
    void getUserRequests_ShouldThrowWhenUserNotFound() {
        assertThrows(NotFoundUserException.class, () ->
                itemRequestService.getUserRequests(999L));
    }

    @Test
    void getAllRequests_ShouldReturnAllRequestsOrderedByDate() {
        List<ItemRequest> requests = itemRequestService.getAllRequests();

        assertThat(requests, hasSize(2));
        // Проверяем сортировку по дате (новые сначала)
        assertThat(requests.get(0).getId(), equalTo(request2.getId()));
        assertThat(requests.get(1).getId(), equalTo(request1.getId()));
    }

    @Test
    void getRequestById_ShouldReturnRequest() {
        ItemRequest found = itemRequestService.getRequestById(request1.getId());

        assertThat(found.getId(), equalTo(request1.getId()));
        assertThat(found.getDescription(), equalTo("Нужна дрель"));
    }

    @Test
    void getRequestById_ShouldThrowWhenNotFound() {
        assertThrows(NotFoundRequestException.class, () ->
                itemRequestService.getRequestById(999L));
    }
}