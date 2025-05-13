package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ExistEmailException;
import ru.practicum.shareit.user.dto.UpdateUserRequest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Repository
public class InMemoryUserRepository {
    private final HashMap<Long, User> users = new HashMap<>();

    public Collection<User> findAllUsers() {
        return users.values();
    }

    public Optional<User> findUserById(long userId) {
        if (users.containsKey(userId)) {
            return Optional.of(users.get(userId));
        }
        return Optional.empty();
    }

    public User createUser(User user) {
        isExistEmail(user.getEmail());
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(UpdateUserRequest updateUserRequest) {
        isExistEmail(updateUserRequest.getEmail());
        User user = users.get(updateUserRequest.getId());
        return UserMapper.updateUserFields(updateUserRequest, user);

    }

    public User deleteUser(long userId) {
        User user = findUserById(userId).get();
        users.remove(userId);
        return user;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void isExistEmail(String email) {
        if (users.values().stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email))) {
            throw new ExistEmailException(email);
        }
    }
}
