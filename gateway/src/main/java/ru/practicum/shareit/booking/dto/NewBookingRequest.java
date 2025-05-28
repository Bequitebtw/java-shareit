package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.annotation.ValidBookingTime;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ValidBookingTime
@Builder
public class NewBookingRequest {
    private Long itemId;
    @NotNull
    @FutureOrPresent
    private LocalDateTime start;
    @Future
    @NotNull
    private LocalDateTime end;
}