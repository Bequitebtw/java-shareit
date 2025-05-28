package ru.practicum.shareit;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UpdateUserRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@Rollback
@SpringBootTest(
        properties = "spring.profiles.active=h2",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplTest {
    private final UserServiceImpl userService;
    private final UserRepository userRepository;
    private User user;
    private UpdateUserRequest updateUserRequest;

    @BeforeEach
    void setup() {
        user = new User();
        user.setName("Алексей");
        user.setEmail("alex@gmail.com");

        updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setName("Артем");
        updateUserRequest.setEmail("artem@gmail.com");
    }

    @Test
    void updateUserTest() {
        userRepository.save(user);
        userService.updateUser(updateUserRequest, user.getId());
        User testUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(testUser.getName(), equalTo(updateUserRequest.getName()));
        assertThat(testUser.getEmail(), equalTo(updateUserRequest.getEmail()));

    }

}
