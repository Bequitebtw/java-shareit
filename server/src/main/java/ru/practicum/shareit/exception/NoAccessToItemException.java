package ru.practicum.shareit.exception;


public class NoAccessToItemException extends RuntimeException {
    private final long userId;
    private final long itemId;

    public NoAccessToItemException(long userId, long itemId) {
        this.itemId = itemId;
        this.userId = userId;
    }

    @Override
    public String getMessage() {
        return "У пользователя " + userId + " нет доступа к предмету " + itemId;
    }
}
