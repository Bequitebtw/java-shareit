package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final JpaUserRepository userRepository;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }

    public UserDto getUserById(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException(userId));
        return UserMapper.mapToUserDto(user);
    }

    public UserDto createUser(User user) {
        return UserMapper.mapToUserDto(userRepository.save(user));
    }

    public UserDto updateUser(UpdateUserRequest updateUserRequest, long userId) {
        updateUserRequest.setId(userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException(userId));
        return UserMapper.mapToUserDto(userRepository.save(UserMapper.updateUserFields(updateUserRequest, user)));
    }

    public UserDto deleteUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException(userId));
        userRepository.delete(user);
        return UserMapper.mapToUserDto(user);
    }

}
