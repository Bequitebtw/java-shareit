package ru.practicum.shareit;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@Rollback
@SpringBootTest(
        properties = "spring.profiles.active=h2",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplTest {
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private User user;
    private User otherUser;

    @BeforeEach
    void setup() {
        user = new User();
        user.setName("Алексей");
        user.setEmail("alex@gmail.com");
        userRepository.save(user);

        otherUser = new User();
        otherUser.setName("Артем");
        otherUser.setEmail("artem@gmail.com");
        userRepository.save(otherUser);
    }

    @Test
    void getUserItems() {
        Item item1 = new Item();
        item1.setName("Машина");
        item1.setUser(user);

        Item item2 = new Item();
        item2.setName("Книга");
        item2.setUser(user);

        itemRepository.save(item1);
        itemRepository.save(item2);

        List<ItemDto> userItems = itemService.getUserItems(user.getId());
        assertThat(userItems.get(0).getName(), equalTo(item1.getName()));
        assertThat(userItems.get(1).getName(), equalTo(item2.getName()));
    }


}
