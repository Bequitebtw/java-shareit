package ru.practicum.shareit.booking.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.annotation.ValidBookingTime;
import ru.practicum.shareit.booking.dto.BookingNewRequest;

public class BookingTimeValidator implements ConstraintValidator<ValidBookingTime, BookingNewRequest> {

    @Override
    public boolean isValid(BookingNewRequest dto, ConstraintValidatorContext context) {
        if (dto.getStart() == null || dto.getEnd() == null) {
            return true;
        }
        return dto.getStart().isBefore(dto.getEnd());
    }
}