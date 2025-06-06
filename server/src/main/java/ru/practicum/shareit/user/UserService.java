package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(long userId);

    UserDto createUser(User user);

    UserDto updateUser(UpdateUserRequest updateUserRequest, long userId);

    UserDto deleteUser(long userId);

}
