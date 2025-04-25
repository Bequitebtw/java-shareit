package ru.practicum.shareit.exception;

public class NotFoundUserException extends RuntimeException {
    private final long userId;

    public NotFoundUserException(long userId) {
        this.userId = userId;
    }

    @Override
    public String getMessage() {
        return "Пользователь с id: " + userId + " не найден";
    }
}
