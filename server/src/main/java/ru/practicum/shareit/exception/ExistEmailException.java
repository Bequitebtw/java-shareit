package ru.practicum.shareit.exception;

public class ExistEmailException extends RuntimeException {

    private final String email;

    public ExistEmailException(String email) {
        this.email = email;
    }

    @Override
    public String getMessage() {
        return "Email - " + email + " уже существует";
    }
}
