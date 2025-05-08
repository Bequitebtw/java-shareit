package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingNewRequest;

import java.util.List;

public interface BookingService {

    BookingDto createBookingRequest(long userId, BookingNewRequest bookingNewRequest);
    BookingDto approveBookingRequest(long userId,long bookingId,boolean isApproved);
    BookingDto getBookingById(long userId,long bookingId);
    List<BookingDto> getBookings(long userId, String state);
    List<BookingDto> getOwnerBookings(long userId,String state);


}
