package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.BadRequestException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Repository
public class UserRepository {
    private final HashMap<Long,User> users = new HashMap<>();

    public Collection<User> findAllUsers(){
        return users.values();
    }

    public Optional<User> findUserById(long userId){
         if(users.containsKey(userId)) {
             return Optional.of(users.get(userId));
         } return Optional.empty();
    }

    public User createUser(User user){
        user.setId(getNextId());
        users.put(user.getId(),user);
        return user;
    }

    public User updateUser(User user){
        users.put(user.getId(),user);
        return user;
    }

    public User deleteUser(long userId){
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
}
