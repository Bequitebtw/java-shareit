package ru.practicum.shareit.exception;

public class NotAvailableItemException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Предмет который вы хотите забронировать недоступен";
    }
}
