package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingNewRequest;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final String userIdHeader = "X-Sharer-User-Id";
    private final BookingService bookingService;
    @PostMapping
    public BookingDto createBookingRequest(@RequestHeader(userIdHeader) long userId, @Valid @RequestBody BookingNewRequest bookingNewRequest){
        return bookingService.createBookingRequest(userId,bookingNewRequest);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBookingRequest(@RequestHeader(userIdHeader) long userId, @PathVariable long bookingId, @RequestParam Boolean approved){
        return bookingService.approveBookingRequest(userId,bookingId,approved);
    }

    //Может получить либо владелец, либо тот, кто бронирует
    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(userIdHeader) long userId, @PathVariable long bookingId){
        return bookingService.getBookingById(userId,bookingId);
    }


    //Для того кто бронирует, что он забронировал
    @GetMapping()
    public List<BookingDto> getBookings(@RequestHeader(userIdHeader) long userId, @RequestParam(defaultValue = "ALL") String state){
        return bookingService.getBookings(userId,state);
    }

    //Для владельца, что у него забронировали
    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestHeader(userIdHeader) long userId, @RequestParam(defaultValue = "ALL") String state){
        //сортировка по дате от более новых к старым
        return bookingService.getOwnerBookings(userId,state);
    }
}
