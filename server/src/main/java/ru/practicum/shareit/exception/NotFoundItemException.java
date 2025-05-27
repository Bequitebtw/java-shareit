package ru.practicum.shareit.exception;

public class NotFoundItemException extends RuntimeException {

    private final long itemId;

    public NotFoundItemException(long itemId) {
        this.itemId = itemId;
    }

    @Override
    public String getMessage() {
        return "Вещь с id " + itemId + " не найдена";
    }
}
