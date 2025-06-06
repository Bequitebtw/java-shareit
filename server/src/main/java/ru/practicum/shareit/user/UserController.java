package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping()
    public UserDto createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UpdateUserRequest updateUserRequest, @PathVariable long userId) {
        return userService.updateUser(updateUserRequest, userId);
    }

    @DeleteMapping("/{userId}")
    public UserDto deleteUser(@PathVariable long userId) {
        return userService.deleteUser(userId);
    }
}