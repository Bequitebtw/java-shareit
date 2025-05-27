package ru.practicum.shareit;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@Rollback
@SpringBootTest(
        properties = "jdbc.url=jdbc:postgresql://localhost:5432/shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringJUnitConfig
public class ItemRequestServiceImplTest {

    private final ItemRequestService itemRequestService;
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private User user;
    private ItemRequest itemRequest;

    @BeforeEach
    void setup() {
        user = new User();
        user.setName("Алексей");
        user.setEmail("alex@gmail.com");
        userRepository.save(user);


        itemRequest = new ItemRequest();
        itemRequest.setRequester(user);
        itemRequest.setDescription("Мне нужна машина");


    }

    @Test
    void getUserRequests() {
        itemRequestRepository.save(itemRequest);
        List<ItemRequest> requests = itemRequestService.getUserRequests(user.getId());
        assertThat(requests.get(0).getDescription(), equalTo(itemRequest.getDescription()));
        assertThat(requests.get(0).getRequester().getName(), equalTo(user.getName()));

    }


}
