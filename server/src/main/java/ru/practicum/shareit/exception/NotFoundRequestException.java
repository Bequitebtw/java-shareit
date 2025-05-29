package ru.practicum.shareit.exception;

public class NotFoundRequestException extends RuntimeException {
    private final long requestId;

    public NotFoundRequestException(long requestId) {
        this.requestId = requestId;
    }

    @Override
    public String getMessage() {
        return "Запрос с id " + requestId + " не найден";
    }
}
