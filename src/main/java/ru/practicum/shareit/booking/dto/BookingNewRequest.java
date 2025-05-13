package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.annotation.ValidBookingTime;

import java.time.LocalDateTime;

@Getter
@Setter
@ValidBookingTime
public class BookingNewRequest {
    @NotNull
    private long itemId;
    @NotNull
    @Future
    private LocalDateTime start;
    @Future
    @NotNull
    private LocalDateTime end;
}
