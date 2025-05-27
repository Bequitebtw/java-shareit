package ru.practicum.shareit.booking.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.annotation.ValidBookingTime;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

public class BookingTimeValidator implements ConstraintValidator<ValidBookingTime, NewBookingRequest> {

    @Override
    public boolean isValid(NewBookingRequest dto, ConstraintValidatorContext context) {
        if (dto.getStart() == null || dto.getEnd() == null) {
            return true;
        }
        return dto.getStart().isBefore(dto.getEnd());
    }
}