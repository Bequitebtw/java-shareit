package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingNewRequest {
    private long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
