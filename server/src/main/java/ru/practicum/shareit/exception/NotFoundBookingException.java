package ru.practicum.shareit.exception;

public class NotFoundBookingException extends RuntimeException {

    private final long bookingId;

    public NotFoundBookingException(long itemId) {
        this.bookingId = itemId;
    }

    @Override
    public String getMessage() {
        return "Бронирование с id " + bookingId + " не найдено";
    }
}
