package ru.practicum.shareit.exception;

public class NoAccessToBookingException extends RuntimeException {
    private final long userId;
    private final long bookingId;

    public NoAccessToBookingException(long userId, long bookingId) {
        this.bookingId = bookingId;
        this.userId = userId;
    }

    @Override
    public String getMessage() {
        return "У пользователя " + userId + " нет доступа к брони " + bookingId;
    }
}