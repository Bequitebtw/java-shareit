package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@Rollback
@SpringBootTest(
        properties = "spring.profiles.active=h2",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplTest {
    private final UserService userService;
    private final UserRepository userRepository;

    private User user1;
    private User user2;
    private UpdateUserRequest updateRequest;

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

        updateRequest = new UpdateUserRequest();
        updateRequest.setName("UpdatedName");
        updateRequest.setEmail("updated@email.com");
    }

//    @Test
//    void getAllUsers_ShouldReturnAllUsers() {
//        List<UserDto> users = userService.getAllUsers();
//
//        assertThat(users, hasSize(2));
//        assertThat(users.get(0).getName(), equalTo("User1"));
//        assertThat(users.get(1).getName(), equalTo("User2"));
//    }
//
//    @Test
//    void getUserById_ShouldReturnUser() {
//        UserDto user = userService.getUserById(user1.getId());
//
//        assertThat(user.getId(), equalTo(user1.getId()));
//        assertThat(user.getName(), equalTo("User1"));
//        assertThat(user.getEmail(), equalTo("user1@email.com"));
//    }
//
//    @Test
//    void getUserById_ShouldThrowWhenNotFound() {
//        assertThrows(NotFoundUserException.class, () ->
//                userService.getUserById(999L));
//    }

    @Test
    void createUser_ShouldCreateNewUser() {
        User newUser = new User();
        newUser.setName("NewUser");
        newUser.setEmail("new@email.com");

        UserDto created = userService.createUser(newUser);

        assertThat(created.getId(), notNullValue());
        assertThat(created.getName(), equalTo("NewUser"));
        assertThat(created.getEmail(), equalTo("new@email.com"));
    }

//    @Test
//    void updateUser_ShouldUpdateAllFields() {
//        UserDto updated = userService.updateUser(updateRequest, user1.getId());
//
//        assertThat(updated.getName(), equalTo("UpdatedName"));
//        assertThat(updated.getEmail(), equalTo("updated@email.com"));
//    }
//
//    @Test
//    void updateUser_ShouldUpdatePartialFields() {
//        UpdateUserRequest partialUpdate = new UpdateUserRequest();
//        partialUpdate.setEmail("partial@email.com");
//
//        UserDto updated = userService.updateUser(partialUpdate, user1.getId());
//
//        assertThat(updated.getName(), equalTo("User1")); // Имя не изменилось
//        assertThat(updated.getEmail(), equalTo("partial@email.com"));
//    }
//
//    @Test
//    void updateUser_ShouldThrowWhenUserNotFound() {
//        assertThrows(NotFoundUserException.class, () ->
//                userService.updateUser(updateRequest, 999L));
//    }
//
//    @Test
//    void deleteUser_ShouldDeleteUser() {
//        UserDto deleted = userService.deleteUser(user1.getId());
//
//        assertThat(deleted.getName(), equalTo("User1"));
//        assertFalse(userRepository.existsById(user1.getId()));
//    }
//
//    @Test
//    void deleteUser_ShouldThrowWhenUserNotFound() {
//        assertThrows(NotFoundUserException.class, () ->
//                userService.deleteUser(999L));
//    }
}