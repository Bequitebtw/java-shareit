package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {
    private final JpaUserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException(userId));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto createUser(User user) {
        return UserMapper.mapToUserDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(UpdateUserRequest updateUserRequest, long userId) {
        updateUserRequest.setId(userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException(userId));
        return UserMapper.mapToUserDto(userRepository.save(UserMapper.updateUserFields(updateUserRequest, user)));
    }

    @Override
    public UserDto deleteUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException(userId));
        userRepository.delete(user);
        return UserMapper.mapToUserDto(user);
    }

}
