package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingNewRequest;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final String userIdHeader = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader(userIdHeader) long userId, @RequestBody BookingNewRequest bookingNewRequest) {
        return bookingService.createBookingRequest(userId, bookingNewRequest);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBookingRequest(@RequestHeader(userIdHeader) long userId, @PathVariable long bookingId, @RequestParam Boolean approved) {
        return bookingService.approveBookingRequest(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(userIdHeader) long userId, @PathVariable long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping()
    public List<BookingDto> getBookings(@RequestHeader(userIdHeader) long userId, @RequestParam String state) {
        return bookingService.getBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestHeader(userIdHeader) long userId, @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getOwnerBookings(userId, state);
    }
}
