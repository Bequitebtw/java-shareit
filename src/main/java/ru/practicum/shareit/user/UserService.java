package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public List<UserDto> getAllUsers() {
        return userRepository.findAllUsers().stream().map(UserMapper::mapToUserDto).toList();
    }

    public UserDto getUserById(long userId) {
        User user = userRepository.findUserById(userId).orElseThrow(() -> new NotFoundUserException(userId));
        return UserMapper.mapToUserDto(user);
    }

    public UserDto createUser(User user) {
        return UserMapper.mapToUserDto(userRepository.createUser(user));
    }

    public UserDto updateUser(User user) {
        if (user.getId()==null){
            throw new BadRequestException("для обновления нужно передать id");
        }
        userRepository.findUserById(user.getId()).orElseThrow(() -> new NotFoundUserException(user.getId()));
        return UserMapper.mapToUserDto(userRepository.updateUser(user));
    }

    public UserDto deleteUser(long userId) {
        userRepository.findUserById(userId).orElseThrow(() -> new NotFoundUserException(userId));
        return UserMapper.mapToUserDto(userRepository.deleteUser(userId));
    }

}
